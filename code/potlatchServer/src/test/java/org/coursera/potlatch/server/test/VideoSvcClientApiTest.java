package org.coursera.potlatch.server.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.UUID;

import org.coursera.potlatch.server.TestData;
import org.coursera.potlatch.server.client.PotlatchSvcApi;
import org.coursera.potlatch.server.client.SecuredRestBuilder;
import org.coursera.potlatch.server.client.SecuredRestException;
import org.coursera.potlatch.server.repository.Gift;
import org.coursera.potlatch.server.repository.GiftBean;
import org.junit.Test;

import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;

import com.google.gson.JsonObject;

/**
 * 
 * This integration test sends a POST request to the VideoServlet to add a new
 * video and then sends a second GET request to check that the video showed up
 * in the list of videos. Actual network communication using HTTP is performed
 * with this test.
 * 
 * The test requires that the VideoSvc be running first (see the directions in
 * the README.md file for how to launch the Application).
 * 
 * To run this test, right-click on it in Eclipse and select
 * "Run As"->"JUnit Test"
 * 
 * Pay attention to how this test that actually uses HTTP and the test that just
 * directly makes method calls on a VideoSvc object are essentially identical.
 * All that changes is the setup of the videoService variable. Yes, this could
 * be refactored to eliminate code duplication...but the goal was to show how
 * much Retrofit simplifies interaction with our service!
 * 
 * @author jules
 *
 */
public class VideoSvcClientApiTest {

	private final String USERNAME = "admin";
	private final String PASSWORD = "pass";
	private final String CLIENT_ID = "mobile";
	private final String READ_ONLY_CLIENT_ID = "mobileReader";

	private final String TEST_URL = "https://localhost:8443";

	private PotlatchSvcApi videoService = new SecuredRestBuilder()
			.setLoginEndpoint(TEST_URL + PotlatchSvcApi.TOKEN_PATH)
			.setUsername(USERNAME)
			.setPassword(PASSWORD)
			.setClientId(CLIENT_ID)
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
			.create(PotlatchSvcApi.class);

	private PotlatchSvcApi readOnlyVideoService = new SecuredRestBuilder()
			.setLoginEndpoint(TEST_URL + PotlatchSvcApi.TOKEN_PATH)
			.setUsername(USERNAME)
			.setPassword(PASSWORD)
			.setClientId(READ_ONLY_CLIENT_ID)
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
			.create(PotlatchSvcApi.class);

	private PotlatchSvcApi invalidClientVideoService = new SecuredRestBuilder()
			.setLoginEndpoint(TEST_URL + PotlatchSvcApi.TOKEN_PATH)
			.setUsername(UUID.randomUUID().toString())
			.setPassword(UUID.randomUUID().toString())
			.setClientId(UUID.randomUUID().toString())
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
			.create(PotlatchSvcApi.class);

	private GiftBean gift = createGift();

	private static GiftBean createGift() {
		return TestData.randomGift();
	}

	/**
	 * This test creates a Video, adds the Video to the VideoSvc, and then
	 * checks that the Video is included in the list when getVideoList() is
	 * called.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testVideoAddAndList() throws Exception {
		// Collection<? extends Gift> videosBefore = videoService.getGiftList();
		// Add the video
		videoService.addChain(gift);

		// We should get back the video that we added above
		Collection<? extends Gift> videos = videoService.getGiftList();
		assertTrue(videos.contains(gift));
	}

	/**
	 * This test ensures that clients with invalid credentials cannot get
	 * access to videos.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAccessDeniedWithIncorrectCredentials() throws Exception {

		try {
			// Add the video
			invalidClientVideoService.addChain(gift);

			fail("The server should have prevented the client from adding a video"
					+ " because it presented invalid client/user credentials");
		} catch (RetrofitError e) {
			assert (e.getCause() instanceof SecuredRestException);
		}
	}
	
	/**
	 * This test ensures that read-only clients can access the video list
	 * but not add new videos.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testReadOnlyClientAccess() throws Exception {

		Collection<? extends Gift> videos = readOnlyVideoService.getGiftList();
		assertNotNull(videos);
		
		try {
			// Add the video
			readOnlyVideoService.addChain(gift);

			fail("The server should have prevented the client from adding a video"
					+ " because it is using a read-only client ID");
		} catch (RetrofitError e) {
			JsonObject body = (JsonObject)e.getBodyAs(JsonObject.class);
			assertEquals("insufficient_scope", body.get("error").getAsString());
		}
	}

	@Test
	public void testFindGiftByChainId() throws Exception {
		// Collection<? extends Gift> videosBefore = videoService.getGiftList();
		// Add the video
		GiftBean chain1 = createGift();
		GiftBean chain2 = createGift();
		GiftBean gift11 = createGift();
		GiftBean gift12 = createGift();
		GiftBean gift21 = createGift();
		GiftBean gift22 = createGift();
		addChains(chain1, chain2);
		// add them to chains explicitly
		addGiftsToChain(chain1, gift11, gift12);
		addGiftsToChain(chain2, gift21, gift22);

		Collection<GiftBean> giftsInChain1 = videoService
				.findGiftsByChainId(chain1.getId());
		Collection<GiftBean> giftsInChain2 = videoService
				.findGiftsByChainId(chain2
				.getId());
		assertTrue(giftsInChain1.contains(gift11));
		assertTrue(giftsInChain1.contains(gift12));
		assertFalse(giftsInChain1.contains(gift21));
		assertFalse(giftsInChain1.contains(gift22));

		assertFalse(giftsInChain2.contains(gift11));
		assertFalse(giftsInChain2.contains(gift12));
		assertTrue(giftsInChain2.contains(gift21));
		assertTrue(giftsInChain2.contains(gift22));
	}

	@Test
	public void testUpdateGift() throws Exception {
		GiftBean gift1 = createGift();
		GiftBean gift2 = createGift();
		GiftBean gift3 = createGift();
		gift1.setTouchCount(83L);
		gift2.setTouchCount(783L);
		addChains(gift1, gift2, gift3);
		gift1.setTitle("New Title");
		long idOne = gift1.getId();
		videoService.updateGift(gift1, idOne);

		Collection<GiftBean> storedGifts = videoService.getGiftList();

		assertTrue(storedGifts.contains(gift1));
	}

	@Test
	public void testFindByTitle() throws Exception {
		GiftBean gift1 = createGift();
		GiftBean gift2 = createGift();
		GiftBean gift3 = createGift();
		String title = gift1.getTitle();

		addChains(gift1, gift2, gift3);

		long idOne = gift1.getId();
		Collection<GiftBean> foundGifts = videoService.findByTitle(title);
		assertTrue(foundGifts.contains(gift1));
		assertEquals(1, foundGifts.size());
		assertEquals(idOne, foundGifts.iterator().next().getId());
	}

	@Test
	public void testTopGifts() throws Exception {
		long currentMaxTouchCount = getMaxTouchCount();
		GiftBean gift1 = createGift();
		GiftBean gift2 = createGift();
		GiftBean gift3 = createGift();
		gift1.setTouchCount(83L);
		gift2.setTouchCount(currentMaxTouchCount + 2);
		addChains(gift1, gift2, gift3);

		Iterable<GiftBean> topGifts = videoService.getTopGifts();
		GiftBean mostPopular = topGifts.iterator().next();
		assertEquals(gift2, mostPopular);
	}

	private long getMaxTouchCount() {
		Collection<GiftBean> allGifts = videoService.getGiftList();
		long max = 0;
		for (GiftBean gift : allGifts) {
			max = max < gift.getTouchCount() ? gift.getTouchCount() : max;
		}
		return max;
	}

	private void addChains(GiftBean... gifts) {
		for (GiftBean gift : gifts) {
			long giftId = videoService.addChain(gift);
			assertNotEquals("failed to add gift: " + gift, 0L, giftId);
			gift.setId(giftId);
		}
	}

	private void addGiftsToChain(GiftBean chain, GiftBean... gifts) {
		Long chainId = chain.getId();
		assertNotEquals("invalid chain w/o id: " + chain, Long.valueOf(0),
				chainId);

		for (GiftBean gift : gifts) {
			long giftId = videoService.addGift(gift, chainId);
			assertNotEquals("failed to add gift: " + gift, 0L, giftId);
			gift.setId(giftId);
		}
	}

}
