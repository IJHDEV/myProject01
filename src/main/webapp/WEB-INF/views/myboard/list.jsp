<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="contextPath" value="${pageContext.request.contextPath }"/>

<%@include file="../myinclude/myheader.jsp" %>

<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h3 class="page-header">Board - List</h3>
        </div><%-- /.col-lg-12 --%>
    </div><%-- /.row --%>
    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                	<div class="row">
		                <div class="col-md-6" style="font-size:20px; height:45px; padding-top:10px;">게시글 목록</div>
		                <div class="col-md-6" style="padding-top:8px;">
		                	<button type="button" id="btnMoveRegister" class="btn btn-primary btn-sm pull-right">새글 등록</button>
		                </div>
		            </div><%-- /.row --%>
               	</div><%-- /.panel-heading --%>
               	<div class="panel-body">
               		<div class="row">
	                	<form class="form-inline" id="frmSendValue" name="frmSendValue" action="${contextPath }/myboard/list" method="get">
							<div class="form-group col-sm-6">
								<label class="sr-only">frmSendValues</label>
								<select class="form-control input-sm" id="selectAmount" name="rowAmountPerPage">
									<option value="10" ${pagingCreator.myBoardPaging.rowAmountPerPage eq 10 ? 'selected' : '' }>10개</option>
									<option value="20" ${pagingCreator.myBoardPaging.rowAmountPerPage eq 20 ? 'selected' : '' }>20개</option>
									<option value="50" ${pagingCreator.myBoardPaging.rowAmountPerPage eq 50 ? 'selected' : '' }>50개</option>
									<option value="100" ${pagingCreator.myBoardPaging.rowAmountPerPage eq 100 ? 'selected' : '' }>100개</option>
								</select>
								
								<select class="form-control input-sm" id="selectScope" name="scope">
									<option value="" ${pagingCreator.myBoardPaging.scope == null ? 'selected' : '' }>선택</option>
									<option value="T" ${pagingCreator.myBoardPaging.scope eq 'T' ? 'selected' : '' }>제목</option>
									<option value="C" ${pagingCreator.myBoardPaging.scope eq 'C' ? 'selected' : '' }>내용</option>
									<option value="W" ${pagingCreator.myBoardPaging.scope eq 'W' ? 'selected' : '' }>작성자</option>
									<option value="TC" ${pagingCreator.myBoardPaging.scope eq 'TC' ? 'selected' : '' }>제목+내용</option>
									<option value="TW" ${pagingCreator.myBoardPaging.scope eq 'TW' ? 'selected' : '' }>제목+작성자</option>
									<option value="CW" ${pagingCreator.myBoardPaging.scope eq 'CW' ? 'selected' : '' }>내용+작성자</option>
									<option value="TCW" ${pagingCreator.myBoardPaging.scope eq 'TCW' ? 'selected' : '' }>제목+내용+작성자</option>
								</select>
								
								<%-- 키워드 검색 --%>
								<input class="form-control input-sm" id="keyword" name="keyword" type="text" placeholder="검색어를 입력하세요"
									   value='<c:out value="${pagingCreator.myBoardPaging.keyword}" />' />
								<span class="form-control-btn">
									<button class="btn btn-warning btn-sm" type="button" id="btnSearchGo"><%-- 
										검색 &nbsp; --%><i class="fa fa-search"></i>
									</button>
									<button id="btnResetKeyword" class="btn btn-info btn-sm" type="button">Reset</button>
								</span>
							</div>
							
							<div class="form-group col-sm-6 text-right">
								<%-- 기간 검색 --%>
								<input class="form-control input-sm" type="date" name="startDate" id="startDate" value="${pagingCreator.myBoardPaging.startDate }">
								<input class="form-control input-sm" type="date" name="endDate" id="endDate" value="${pagingCreator.myBoardPaging.endDate }">
								<span class="form-control-btn">
									<button class="btn btn-warning btn-sm" type="button" id="btnSearchDateGo"><%-- 
										검색 &nbsp; --%><i class="fa fa-search"></i>
									</button>
									<button id="btnResetDate" class="btn btn-info btn-sm" type="button">Reset</button>
								</span>
							</div>
							
							<input type="hidden" name="pageNum" value="${pagingCreator.myBoardPaging.pageNum }"><%-- 
							<input type="hidden" name="rowAmountPerPage" value="${pagingCreator.myBoardPaging.rowAmountPerPage }"> --%>
							<input type="hidden" name="lastPageNum" value="${pagingCreator.lastPageNum }">
						</form>
					</div><%-- /.row --%>
                
                	<br>
                    <table class="table table-striped table-bordered table-hover" style="width:100%;text-align:center;">
                        <thead>
                            <tr>
                                <th style="text-align:center;">글번호</th>
                                <th style="text-align:center;">글제목</th>
                                <th style="text-align:center;">작성자</th>
                                <th style="text-align:center;">작성일시</th>
                                <th style="text-align:center;">수정일시</th>
                                <th style="text-align:center;">조회수</th>
                            </tr>
                        </thead>
                        <tbody>
							<c:forEach items="${boardList }" var="board">
								<c:choose>
									<c:when test="${board.bdelFlag == 1 }">
										<tr style="background-color: Moccasin">
											<td>${board.bno }</td>
											<td colspan="5"><em>작성자에 의해 삭제된 게시글입니다.</em></td>
										</tr>
									</c:when>
									<c:otherwise>
										<tr class="moveDetail" data-bno='<c:out value="${board.bno }"/>'>
											<td><c:out value="${board.bno }"/></td>
											<td style="text-align:left;">
												<c:out value="${board.btitle }"/>
												<small>[댓글수: <strong><c:out value="${board.breplyCnt }"></c:out></strong>]</small>
											</td>
											<td><c:out value="${board.bwriter }"/></td>
											<td><fmt:formatDate value="${board.bregDate }" pattern="yyyy/MM/dd HH:mm:ss"/></td>
											<td><fmt:formatDate value="${board.bmodDate }" pattern="yyyy/MM/dd HH:mm:ss"/></td>
											<td><c:out value="${board.bviewsCnt }"/></td>
										</tr>
									</c:otherwise>
								</c:choose>
							</c:forEach>
                        </tbody>
                    </table><%-- /.table-responsive --%>
                    
                    <%-- Pagination 시작 --%>
                    <div style="text-align: center;">
						<ul class="pagination pagination-md">
							<c:if test="${pagingCreator.prev }">							
								<li class="pagination-button">
									<a href="1" aria-label="Previous">
										<span aria-hidden="true">&laquo;</span>
									</a>
								</li>
							</c:if>
							<c:if test="${pagingCreator.prev }">
								<li class="pagination-button">
									<a href="${pagingCreator.startPagingNum - 1 }" aria-label="Previous">
										<span aria-hidden="true">Prev</span>
									</a>
								</li>
							</c:if>
							<c:forEach var="pageNum" begin="${pagingCreator.startPagingNum }" end="${pagingCreator.endPagingNum }" step="1">
								<li class='pagination-button ${pagingCreator.myBoardPaging.pageNum == pageNum ? "active" : "" }'>
									<a href="${pageNum }">${pageNum }</a>
								</li>
							</c:forEach>
							<c:if test="${pagingCreator.next }">
								<li class="pagination-button">
									<a href="${pagingCreator.endPagingNum + 1 }" aria-label="Next">
										<span aria-hidden="true">Next</span>
									</a>
								</li>
							</c:if>							
							<c:if test="${pagingCreator.next }">							
								<li class="pagination-button">
									<a href="${pagingCreator.lastPageNum }" aria-label="Next">
										<span aria-hidden="true">&raquo;</span>
									</a>
								</li>
							</c:if>							
						</ul>
                    </div>
                    
                </div><%-- /.panel-body --%>
            </div><%-- /.panel --%>
        </div><%-- /.col-lg-12 --%>
    </div><%-- /.row --%>
</div><%-- /#page-wrapper --%>

<%-- Modal --%>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">처리 결과</h4>
            </div>
            <div class="modal-body" id="myModal-body">처리 현황</div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div><%-- /.modal-content --%>
    </div><%-- /.modal-dialog --%>
</div><%-- /.modal --%>


<script>

var frmSendValue = $("#frmSendValue");


<%-- 표시행수 변경 이벤트 처리 --%>
$("#selectAmount").on("change", function(){
	frmSendValue.find("input[name='pageNum']").val(1);
//	frmSendValue.attr("action", "${contextPath}/myboard/list").attr("method", "get");
	
	frmSendValue.submit();
});


<%-- scope 변경 이벤트 처리 --%>
$("#selectScope").on("change", function(){
	frmSendValue.find("input[name='pageNum']").val(1);
//	frmSendValue.attr("action", "${contextPath}/myboard/list").attr("method", "get");
	
	frmSendValue.submit();
});


<%-- keyword 검색버튼 클릭 이벤트 처리 --%>
$("#btnSearchGo").on("click", function(){
	var scope = $("#selectScope").find("option:selected").val();
	if(!scope || scope == "") {
		alert("검색범위를 지정해주세요");
		return false;
	}
	
	var keyword = $("#keyword").val();
	if(!keyword || keyword == "") {
		alert("검색어를 입력해주세요.");
		return;
	}
	
	frmSendValue.find("input[name='pageNum']").val(1);
	frmSendValue.submit();
});


<%-- keyword 검색 초기화버튼 클릭 이벤트 처리 --%>
$("#btnResetKeyword").on("click", function(){
	$("#selectAmount").val(10);
	$("#selectScope").val("");
	$("#keyword").val("");
	$("#lastPageNum").val("");
	
	frmSendValue.submit();
});


<%-- date 검색 이벤트 처리 --%>
$("#btnSearchDateGo").on("click", function(){
	var startDate = $("#startDate").val();
	if (!startDate || startDate == "") {
		alert("검색 시작 날짜를 지정해주세요.");
		return;
	}
	
	var endDate = $("#endDate").val();
	if (!endDate || endDate == "") {
		alert("검색 종료 날짜를 지정해주세요.");
		return;
	}
	
	frmSendValue.find("input[name='pageNum']").val(1);
	frmSendValue.submit();
});


<%-- date검색 초기화버튼 클릭 이벤트 처리 --%>
$("#btnResetDate").on("click", function(){
	$("#startDate").val("");
	$("#endDate").val("");
	
	frmSendValue.submit();
});


$("#btnMoveRegister").on("click", function(){
	self.location.href = "${contextPath}/myboard/register";
});


$(".moveDetail").on("click", function(){
	frmSendValue.append("<input type='hidden' name='bno' value='" + $(this).data("bno") +"'>");
	frmSendValue.attr("action", "${contextPath}/myboard/detail");
	frmSendValue.attr("method", "get");
	
	frmSendValue.submit();
});


var result = '<c:out value="${result}"/>';

function checkModal(result) {
	if (result == '' || history.state) {
		return;
	} else if (result == "successRemove") {
		var myMsg = "게시글이 삭제되었습니다.";
	} else if (parseInt(result) > 0) {
		var myMsg = "게시글" + result + "번이 등록되었습니다.";
	} 
		$("#myModal-body").html(myMsg);
		$("#myModal").modal("show");
		myMsg = "";
}


$(".pagination-button a").on("click", function(e){
	e.preventDefault();
	
	frmSendValue.find("input[name='pageNum']").val($(this).attr("href"));
//	frmSendValue.attr("action", "${contextPath}/myboard/list");
//	frmSendValue.attr("method", "get");
	
	frmSendValue.submit();
});


$(document).ready(function(){
	checkModal(result);
	
	window.addEventListener("popstate", function(event){
		history.pushState(null, null, location.href);
	});
	
	history.pushState(null, null, location.href);
});

</script>

 
<%@include file="../myinclude/myfooter.jsp" %>