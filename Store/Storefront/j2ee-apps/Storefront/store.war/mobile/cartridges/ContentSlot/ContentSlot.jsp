<%--
  This renderer calls the "dsp:renderContentItem" for it's contents.
  Mobile version.

  Required Parameters:
    contentItem
      The page slot content item to render.

  NOTE:
    The "endecaUserAgent" request-scoped variable (request attribute), which is used here,
    is set by the "MobileDetectionInterceptor": it's set, when the "Endeca Preview" is enabled
    (AssemblerSettings.previewEnabled = true) and the "Endeca Preview User-Agent" request parameter
    is present in the request (even if it's an empty string).
--%>
<dsp:page>
  <dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
  <dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" value="${originatingRequest.contentItem}"/>

  <c:choose>
    <c:when test="${endecaUserAgent != null}">
      <endeca:includeSlot contentItem="${contentItem}">
        <c:forEach var="element" items="${contentItem.contents}">
          <dsp:renderContentItem contentItem="${element}"/>
        </c:forEach>
      </endeca:includeSlot>
    </c:when>
    <c:otherwise>
      <c:forEach var="element" items="${contentItem.contents}">
        <dsp:renderContentItem contentItem="${element}"/>
      </c:forEach>
    </c:otherwise>
  </c:choose>
</dsp:page>
<%-- @version $Id: //hosting-blueprint/B2CBlueprint/version/10.2/Storefront/j2ee/store.war/mobile/cartridges/ContentSlot/ContentSlot.jsp#4 $$Change: 792662 $--%>
