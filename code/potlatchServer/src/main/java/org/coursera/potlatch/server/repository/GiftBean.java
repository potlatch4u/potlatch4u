package org.coursera.potlatch.server.repository;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Objects.toStringHelper;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.common.base.Objects;

/**
 * Gift
 * 
 * @author nbischof
 * @since Nov 14, 2014
 * 
 */
@Entity
public class GiftBean implements Gift {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * default value: 0 which means: it's also a chain
	 */
	private long chainId;

	private String title;
	private String description;
	private String url;
	private long touchCount;

	public GiftBean() {
	}

	public GiftBean(String title, String description, String url) {
		super();
		this.title = title;
		this.description = description;
		this.url = url;
	}

	public GiftBean(Gift v) {
		this(v.getTitle(), v.getDescription(), v.getUrl());
	}

	public long getChainId() {
		return chainId;
	}

	public void setChainId(long chainId) {
		this.chainId = chainId;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTouchCount() {
		return touchCount;
	}

	public void setTouchCount(long touchCount) {
		this.touchCount = touchCount;
	}

	/**
	 * Two gifts will generate the same hashcode if they have exactly the same
	 * values for their title, description, url.
	 * 
	 */
	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(title, description, url);
	}

	/**
	 * Two Videos are considered equal if they have exactly the same values for
	 * their name, url, and duration.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GiftBean) {
			GiftBean other = (GiftBean) obj;
			// Google Guava provides great utilities for equals too!
			return equal(title, other.title) && equal(url, other.url)
					&& equal(description, other.description);
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return toStringHelper(this).add("id", id).add("title", title)
				.add("description", description).add("url", url)
				.add("chainId", chainId).add("touchCount", touchCount)
				.toString();
	}

}
