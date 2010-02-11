package models;

import java.util.Date;

import siena.Generator;
import siena.Id;
import siena.Model;

public class Comment extends Model {
	@Id(Generator.AUTO_INCREMENT)
	public Long id;

	public Long entryId;
	public Boolean visible;
	public String name;
	public String comment;
	public Date date;

}
