package org.coursera.potlatch.server.client;

import java.util.Collection;

import org.coursera.potlatch.server.repository.GiftBean;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * This interface defines an API for a VideoSvc. The interface is used to
 * provide a contract for client/server interactions. The interface is annotated
 * with Retrofit annotations so that clients can automatically convert the
 * 
 * 
 * @author jules
 * 
 */
public interface PotlatchSvcApi {

	// // The path where we expect the VideoSvc to live
	// public static final String VIDEO_SVC_PATH = "/video";
	//
	// // The path to search videos by title
	// public static final String VIDEO_TITLE_SEARCH_PATH = VIDEO_SVC_PATH
	// + "/search/findByName";
	//
	// // The path to search videos by title
	// public static final String VIDEO_DURATION_SEARCH_PATH = VIDEO_SVC_PATH
	// + "/search/findByDurationLessThan";
	//
	//
	// @GET(VIDEO_SVC_PATH)
	// public Collection<? extends Gift> getVideoList();
	//
	// @POST(VIDEO_SVC_PATH)
	// public boolean addVideo(@Body Gift v);
	//
	// @GET(VIDEO_TITLE_SEARCH_PATH)
	// public Collection<Gift> findByTitle(@Query(TITLE_PARAMETER) String
	// title);
	//
	// //
	// -----------------------------------------------------------------------------------------------------------------

	public static final String PASSWORD_PARAMETER = "password";

	public static final String USERNAME_PARAMETER = "username";

	public static final String TITLE_PARAMETER = "title";

	public static final String CHAIN_ID_PARAMETER = "cid";

	public static final String TOKEN_PATH = "/oauth/token";

	public static final String PL_CHAINS = "/chains";

	public static final String PL_GIFTS = "/gifts";

	/**
	 * TODO: to be removed
	 */
	public static final String PL_GIFTS_ALL = "/allgifts";

	public static final String PL_GIFT_TITLE_SEARCH_PATH = "/gifts/search";

	public static final String PL_CHAIN_TITLE_SEARCH_PATH = "/chains/search";

	public static final String PL_TOP_GIFTS_PATH = "/gifts/top";

	@GET(PL_TOP_GIFTS_PATH)
	public Collection<GiftBean> getTopGifts();

	@POST(PL_CHAINS + "/{id}")
	public long addGift(@Body GiftBean v, @Path("id") Long chainId);

	@POST(PL_CHAINS)
	public long addChain(@Body GiftBean v);

	@PUT(PL_GIFTS + "/{id}")
	public GiftBean updateGift(@Body GiftBean gift, @Path("id") Long id);

	@GET(PL_GIFTS)
	public Collection<GiftBean> findGiftsByChainId(
			@Query(CHAIN_ID_PARAMETER) Long chainId);

	@GET(PL_GIFTS_ALL)
	public Collection<GiftBean> getGiftList();

	@GET(PL_CHAIN_TITLE_SEARCH_PATH)
	public Collection<GiftBean> findByTitle(@Query(TITLE_PARAMETER) String title);
}
