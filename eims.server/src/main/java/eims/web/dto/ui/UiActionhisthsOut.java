package eims.web.dto.ui;

import java.util.List;
import eims.web.dto.table.ActionhisthsDto;

public class UiActionhisthsOut {

	private int totalCnt;
	private List<ActionhisthsDto> actionhisthsOutList;

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	
	public List<ActionhisthsDto> getActionhisthsOutList() {
		return actionhisthsOutList;
	}

	public void setActionhisthsOutList(List<ActionhisthsDto> actionhisthsOutList) {
		this.actionhisthsOutList = actionhisthsOutList;
	}

}