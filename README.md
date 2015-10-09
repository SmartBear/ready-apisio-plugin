# APIs.io Service Plugin

===================

A plugin for Ready! API that allows you to import APIs using APIs.io Service.

Installation
------------

Install the plugin via the integrated Plugin Repository available via the Plugin Manager in Ready! API 1.3 and newer.


Build it yourself
-----------------

You can build the plugin by oneself by cloning this repository locally - make sure you have java and maven 3.X correctly 
installed - and run 

```mvn clean install assembly:single```

in the project folder. The plugin dist.jar will be created in the target folder and can be installed via the 
Plugin Managers' "Load from File" action.


Usage
-----

Once installed there will have two ways to import an API with an APIs.io search engine:

* Via the "Add API With API Search Engine" option on the Project menu in the "Projects" tab
* Via the "Create Project With API Search Engine" option in the "Create project from..." drop-down when creating a new project

In both cases you will be prompted the dialog with the search field, table with search result and import options.
There are 2 kinds of the APIs can be presented in the search result table:
* API with gray color doesn't contain API declaration and can't be imported
* API with black color contains API declaration and can be imported. List of the available declaration formats is shown in the Format cell.

With import options you can easily:

* send ad-hoc requests to the API to explore its functionality
* create functional tests of the API which you can further use to create Load Tests, Security Tests and API Monitors
(in the SoapUI NG module)
* create a load tests of the API (in the LoadUI NG module)
* create a security tests of the API (in the Secure module)
* create a virtualized version of the API for sandboxing/simulation purposes (in the ServiceV module).