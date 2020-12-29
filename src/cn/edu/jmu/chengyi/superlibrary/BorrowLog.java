package cn.edu.jmu.chengyi.superlibrary;

import java.util.Date;

public final class BorrowLog {
	private final int id;
	private final int borrowerId;
	private final int bookId;
	private final String startTime;
	private final String endTime;
	private final boolean isReturn;

	BorrowLog(int id, int borrowerId, int bookId, String startTime, String endTime, boolean isReturn) {
		this.id = id;
		this.borrowerId = borrowerId;
		this.bookId = bookId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.isReturn = isReturn;
	}

	public int getId() {
		return id;
	}
	
	public int getBorrowerId() {
		return borrowerId;
	}

	public int getBookId() {
		return bookId;
	}

	public Date getStartTime() {
		return new Date(Long.parseLong(startTime));
	}

	public Date getEndTime() {
		return new Date(Long.parseLong(endTime));
	}

	public boolean isReturn() {
		return isReturn;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BorrowLog other = (BorrowLog) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BorrowLog [borrowerId=" + borrowerId + ", bookId=" + bookId + ", startTime=" + startTime + ", endTime="
				+ endTime + ", isReturn=" + isReturn + "]";
	}
}
