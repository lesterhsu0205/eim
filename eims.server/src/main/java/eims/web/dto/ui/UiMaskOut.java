package eims.web.dto.ui;

import java.util.List;

import eims.web.dto.table.MaskcdDto;
import eims.web.dto.table.SrsysbsDto;

public class UiMaskOut {

	private int totalCnt;
	private List<MaskcdDto> maskOutList;

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}

	public List<MaskcdDto> getMaskOutList() {
		return maskOutList;
	}

	public void setMaskOutList(List<MaskcdDto> maskOutList) {
		this.maskOutList = maskOutList;
	}
	
	

}