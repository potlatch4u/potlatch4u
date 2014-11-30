package org.coursera.potlatch.server.repository;

import java.util.Collection;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftRepository extends
		PagingAndSortingRepository<GiftBean, Long> {

	// Find all videos with a matching title (e.g., Video.name)
	public Collection<GiftBean> findByTitle(String title);

	public Collection<GiftBean> findByChainId(Long chainId);
	
}
