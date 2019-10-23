<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<jsp:include page="/WEB-INF/views/common/header.jsp">
   <jsp:param name="pageTitle" value="dev검색결과"/>
</jsp:include>
<section id="container">
<table class="table">
      <tr>
         <th scope="col">번호</th>
         <th scope="col">이름</th>
         <th scope="col">나이</th>
         <th scope="col">이메일</th>
         <th scope="col">성별</th>
         <th scope="col">개발가능언어</th>
      </tr>
      <!-- 내용구성 -->
      <c:forEach var="dev" items="${list}">
      <tr>
         <td scope="col">${dev.devNo }</td>
         <td scope="col">${dev.devName }</td>
         <td scope="col">${dev.devAge }</td>
         <td scope="col">${dev.devEmail }</td>
         <td scope="col">${dev.devGender }</td>
         <td scope="col">
            <c:forEach var="lang" items="${dev.devLang }" varStatus="v">
               ${v.index!=0?",":"" }${lang }
            </c:forEach>
         </td>
         <td>
         	<button type="button" class="btn btn-outline-light" onclick="location.href='${path}'">수정</button>

         </td>
         <td>
         	<button type="button" class="btn btn-outline-light" onclick="location.href='${path}/demo/deleteDev.do?devNo=${dev.devNo }'">삭제</button>
         </td>
         
      </tr>
      </c:forEach>
</table>
</section>
<script>

</script>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>