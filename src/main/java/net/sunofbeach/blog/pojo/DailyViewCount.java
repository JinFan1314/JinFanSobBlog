package net.sunofbeach.blog.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import javax.persistence.Column;

@Entity
@Table(name ="tb_daily_view_count" )
public class DailyViewCount {

  	@Id
	private String id;
  	@Column(name = "view_count" )
	private long view_count;
  	@Column(name = "create_time" )
	private Date create_time;
  	@Column(name = "update_time" )
	private Date update_time;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public long getView_count() {
		return view_count;
	}

	public void setView_count(long view_count) {
		this.view_count = view_count;
	}


	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}


	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

}
