package eims.web.dto.ui;

//@JsonInclude(value = Include.NON_ABSENT, content = Include.NON_EMPTY)
//@JsonIgnoreProperties(ignoreUnknown = true)
public class UiMenuTreeInfo {

	private String id;
	private String parentId;
	private String name;
	private int seq;
	private String description;
	private boolean check;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UiMenuTreeOut [id=");
		builder.append(id);
		builder.append(", parentId=");
		builder.append(parentId);
		builder.append(", name=");
		builder.append(name);
		builder.append(", seq=");
		builder.append(seq);
		builder.append(", description=");
		builder.append(description);
		builder.append(", check=");
		builder.append(check);
		builder.append("]");
		return builder.toString();
	}

}
