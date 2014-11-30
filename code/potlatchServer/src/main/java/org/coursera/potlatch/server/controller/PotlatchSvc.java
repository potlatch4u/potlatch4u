package org.coursera.potlatch.server.controller;

import static java.text.MessageFormat.format;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.coursera.potlatch.server.client.PotlatchSvcApi;
import org.coursera.potlatch.server.repository.GiftBean;
import org.coursera.potlatch.server.repository.GiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

/**
 * This simple VideoSvc allows clients to send HTTP POST requests with videos
 * that are stored in memory using a list. Clients can send HTTP GET requests to
 * receive a JSON listing of the videos that have been sent to the controller so
 * far. Stopping the controller will cause it to lose the history of videos that
 * have been sent to it because they are stored in memory.
 * 
 * Notice how much simpler this VideoSvc is than the original VideoServlet?
 * Spring allows us to dramatically simplify our service. Another important
 * aspect of this version is that we have defined a PotlatchSvcApi that provides
 * strong typing on both the client and service interface to ensure that we
 * don't send the wrong paraemters, etc.
 * 
 * @author jules
 * 
 */

// Tell Spring that this class is a Controller that should 
// handle certain HTTP requests for the DispatcherServlet
@Controller
public class PotlatchSvc implements PotlatchSvcApi {
	
	// The VideoRepository that we are going to store our videos
	// in. We don't explicitly construct a VideoRepository, but
	// instead mark this object as a dependency that needs to be
	// injected by Spring. Our Application class has a method
	// annotated with @Bean that determines what object will end
	// up being injected into this member variable.
	//
	// Also notice that we don't even need a setter for Spring to
	// do the injection.
	//
	@Autowired
	private GiftRepository giftRepo;

	// Receives POST requests to /video and converts the HTTP
	// request body, which should contain json, into a Video
	// object before adding it to the list. The @RequestBody
	// annotation on the Video parameter is what tells Spring
	// to interpret the HTTP request body as JSON and convert
	// it into a Video object to pass into the method. The
	// @ResponseBody annotation tells Spring to conver the
	// return value from the method back into JSON and put
	// it into the body of the HTTP response to the client.
	//
	// The VIDEO_SVC_PATH is set to "/video" in the PotlatchSvcApi
	// interface. We use this constant to ensure that the 
	// client and service paths for the VideoSvc are always
	// in synch.
	//
//	@RequestMapping(value = PotlatchSvcApi.PL_GIFTS, method = RequestMethod.POST)
//	public @ResponseBody
//	boolean addVideo(@RequestBody Gift v) {
//		giftRepo.save(new GiftBean(v));
//		return true;
//	}
	
	// Receives GET requests to /video and returns the current
	// list of videos in memory. Spring automatically converts
	// the list of videos to JSON because of the @ResponseBody
	// annotation.
	// @RequestMapping(value = PotlatchSvcApi.PL_GIFTS, method =
	// RequestMethod.GET)
	// public @ResponseBody
	// Collection<? extends Gift> getVideoList() {
	// return Lists.newArrayList(giftRepo.findAll());
	// }
	
	// Receives GET requests to /video/find and returns all Videos
	// that have a title (e.g., Video.name) matching the "title" request
	// parameter value that is passed by the client
	// @RequestMapping(value = PotlatchSvcApi.PL_GIFT_TITLE_SEARCH_PATH, method
	// = RequestMethod.GET)
	// public @ResponseBody
	// Collection<Gift> findByTitle(
	// // Tell Spring to use the "title" parameter in the HTTP request's query
	// // string as the value for the title method parameter
	// @RequestParam(TITLE_PARAMETER) String title
	// ){
	// return giftRepo.findByName(title);
	// }

	@Override
	@RequestMapping(value = PotlatchSvcApi.PL_TOP_GIFTS_PATH, method = RequestMethod.GET)
	public @ResponseBody
	Collection<GiftBean> getTopGifts() {
		// TODO: it's a List, but better convert or register an InstanceCreator
		return (Collection<GiftBean>) giftRepo.findAll(new Sort(Direction.DESC,
				"touchCount"));
	}

	@Override
	@RequestMapping(value = PotlatchSvcApi.PL_CHAINS + "/{id}", method = RequestMethod.POST)
	public @ResponseBody
	long addGift(@RequestBody GiftBean v, @PathVariable("id") Long chainId) {
		d("adding gift {0} to chain {1} ...", v.getId(), chainId);
		// TODO: assert that this chain exists
		v.setChainId(chainId);
		giftRepo.save(v);
		return v.getId();
	}

	@Override
	@RequestMapping(value = PotlatchSvcApi.PL_CHAINS, method = RequestMethod.POST)
	public @ResponseBody
	long addChain(@RequestBody GiftBean v) {
		return this.addGift(v, 0L);
	}

	@Override
	@RequestMapping(value = PotlatchSvcApi.PL_GIFTS + "/{id}", method = RequestMethod.PUT)
	public @ResponseBody
	GiftBean updateGift(@RequestBody GiftBean gift, @PathVariable("id") Long id) {
		// TODO: issue correct error if gift wasn't found
		GiftBean oldGift = giftRepo.findOne(id);
		gift.setId(id);
		return giftRepo.save(gift);
	}

	@Override
	@RequestMapping(value = PotlatchSvcApi.PL_GIFTS, method = RequestMethod.GET)
	public @ResponseBody
	Collection<GiftBean> findGiftsByChainId(
			@RequestParam(CHAIN_ID_PARAMETER) Long chainId) {
		return giftRepo.findByChainId(chainId);
	}

	@Override
	@RequestMapping(value = PotlatchSvcApi.PL_GIFTS_ALL, method = RequestMethod.GET)
	public @ResponseBody
	Collection<GiftBean> getGiftList() {
		return Lists.newArrayList(giftRepo.findAll());
	}

	@Override
	@RequestMapping(value = PotlatchSvcApi.PL_CHAIN_TITLE_SEARCH_PATH, method = RequestMethod.GET)
	public @ResponseBody
	Collection<GiftBean> findByTitle(@RequestParam(TITLE_PARAMETER) String title) {
		return giftRepo.findByTitle(title);
	}

	private void d(String msg, Object... objects) {
		Logger.getLogger(PotlatchSvc.class).debug(format(msg, objects));
	}

}
