
Scenario: Check 'About Us' link in naviagartion panel
Given crs site
When I open '/crs/storeus/company/aboutUs.jsp'
Then navigation panel contains 'About Us' link to '/crs/storeus/company/aboutUs.jsp'

Scenario: Check 'About Deming' link in naviagartion panel
Given crs site
When I open '/crs/storeus/company/aboutUs.jsp'
Then navigation panel contains 'About Deming' link to 'http://en.wikipedia.org/wiki/W._Edwards_Deming'
