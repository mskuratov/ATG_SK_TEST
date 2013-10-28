
Scenario: Check 'About Us' link in naviagartion panel
Given crs site
When I open '/crs/storeus/company/aboutUs.jsp'
Then navigation panel contains 'About Us' link to '/crs/storeus/company/aboutUs.jsp'

Scenario: Check 'About Grid Dynamics' link in naviagartion panel
Given crs site
When I open '/crs/storeus/company/aboutUs.jsp'
Then navigation panel contains 'About Grid Dynamics' link to 'http://www.griddynamics.com'
