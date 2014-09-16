package com.cnpanoramio.json;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.cnpanoramio.domain.Recycle.RecycleType;
import com.cnpanoramio.json.TravelResponse.Travel;

@XmlRootElement
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class RecycleResponse extends ExceptionResponse {

	private Recycle recycle;
	
	private List<Recycle> recycles;
	
	@XmlRootElement
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public static class Recycle {
		
		private Long id;
		
		@JsonProperty("user_id")
		private Long userId;
		
		@JsonProperty("create_time")
		private Date createTime;
		
		@JsonProperty("recy_type")
		private RecycleType recyType;
		
		@JsonProperty("recy_id")
		private Long recyId;
		
		private PhotoProperties photo;
		
		private Travel travel;
		
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		public RecycleType getRecyType() {
			return recyType;
		}

		public void setRecyType(RecycleType recyType) {
			this.recyType = recyType;
		}

		public Long getRecyId() {
			return recyId;
		}

		public void setRecyId(Long recyId) {
			this.recyId = recyId;
		}

		public PhotoProperties getPhoto() {
			return photo;
		}

		public void setPhoto(PhotoProperties photo) {
			this.photo = photo;
		}

		public Travel getTravel() {
			return travel;
		}

		public void setTravel(Travel travel) {
			this.travel = travel;
		}
		
		
		
	}

	public Recycle getRecycle() {
		return recycle;
	}

	public void setRecycle(Recycle recycle) {
		this.recycle = recycle;
	}

	public List<Recycle> getRecycles() {
		return recycles;
	}

	public void setRecycles(List<Recycle> recycles) {
		this.recycles = recycles;
	}
	
	
}
