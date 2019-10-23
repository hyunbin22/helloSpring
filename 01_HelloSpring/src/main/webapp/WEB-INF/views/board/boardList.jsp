<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<jsp:include page="/WEB-INF/views/common/header.jsp">
	<jsp:param name="pageTitle" value="게시판"/>
</jsp:include>
<section id="content">
	<p> 총 <fmt:formatNumber value="${count }" pattern="#,#00"/>건의 게시물이 있습니다.</p>
   <input type="button" value="글쓰기" id='btn-add' class='btn btn-outline-success' onclick='location.href="${path}/board/boardForm"'/>

   <table id='tbl-board' class='table table-striped table-hover'>
      <tr>
         <th>번호</th>
         <th>제목</th>
         <th>작성자</th>
         <th>작성일</th>
         <th>첨부파일</th>
         <th>조회수</th>         
      </tr>
	<c:forEach items="${list }" var="b" varStatus="v">
		<tr>
			<td>
				<c:if test="${param.cPage!=1 }">
					<c:out value="${v.count+5 }"/>
				</c:if>
				<c:if test="${param.cPage==1 }">
					<c:out value="${v.count }"/>
				</c:if>
			</td>
			<td>
				<a href='${path}/board/boardView.do?boardNo=${b["BOARDNO"]}'>
					<c:out value='${b["BOARDTITLE"] }'/>
				</a>
			</td>
			<td><c:out value='${b["BOARDWRITER"] }'/></td>
			<td><c:out value='${b["BOARDDATE"] }'/></td>
			<td align='center'>
				<c:if test='${b["ATTACHCOUNT"]>0}'>
					<img src="${path }/resources/images/file.png" width="16px"/>
				</c:if>
			</td>
			<td><c:out value='${b["BOARDREADCOUNT"] }'/></td>
		</tr>
	</c:forEach>
   </table>
  ${pageBar }
	
</section>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>






