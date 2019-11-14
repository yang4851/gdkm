<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% 

String urlSt = request.getRequestURL (). toString ();    
String jnlpCodeBase = urlSt.substring (0, urlSt.lastIndexOf ( '/'));    
String jnlpRefURL = urlSt.substring (urlSt.lastIndexOf ( '/') + 1, urlSt.length ());

response.setContentType ( "application / x-java-jnlp-file");  
response.setHeader ( "Cache-Control", null); 
response.setHeader ( "Set-Cookie", null); 
response.setHeader ( "Vary", null);

request.setAttribute("jnlpCodeBase", jnlpCodeBase);
request.setAttribute("id", request.getParameter("id"));
%>

<c:url value='${jnlpCodeBase}/achieves/${id}/gview.gff' var="GetGffFile">
	<c:param name="achiveId" value="${id}"></c:param>
</c:url> 

<jnlp spec="1.0+" codebase="<%= jnlpCodeBase %>" href="gview.jsp?id=<%=request.getParameter("id")%>"> 

  <information> 
    <title>GView</title> 
    <vendor>Bioinformatics Corefacility</vendor> 
    <homepage href="<%= jnlpCodeBase %>"/> 
    <description>GView</description>
    <description kind="short">Java-based interactive genome viewer.</description> 
    <icon href="gview.gif"/> 
    <offline-allowed/> 
  </information> 
  <security> 
    <all-permissions/> 
  </security> 
  <resources>
    <j2se version="1.6+" max-heap-size="500M"/>
    <jar href="<%= jnlpCodeBase %>/gview.jar"/>
  </resources>
  <application-desc >
    <argument>-i</argument>
    <argument>${GetGffFile}</argument>
    <argument>-s</argument>
    <argument><%= jnlpCodeBase %>/sample/basicStyle.gss</argument>
    <argument>-v</argument>
    <argument>-l</argument>
    <argument>circular</argument>
  </application-desc>
</jnlp>
