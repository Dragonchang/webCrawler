<#macro commonLeft>
    <!-- Main Sidebar Container -->
    <aside class="main-sidebar">
        <!-- sidebar: style can be found in sidebar.less -->
        <section class="sidebar">
            <!-- sidebar menu: : style can be found in sidebar.less -->
            <ul class="sidebar-menu">
                <li class="nav-click " ><a href="${request.contextPath}/"><i class="fa fa-circle-o text-aqua"></i><span>${"Dashboard"}</span></a></li>
                <li class="nav-click " ><a href="${request.contextPath}/company"><i class="fa fa-circle-o text-yellow"></i><span>${"Company"}</span></a></li>
            </ul>
        </section>
        <!-- /.sidebar -->
    </aside>
</#macro>


<#macro commonScript>
    <!-- jQuery 2.1.4 -->
    <script src="${request.contextPath}/js/adminlte/bower_components/jquery/jquery.min.js"></script>
    <!-- Bootstrap 3.3.5 -->
    <script src="${request.contextPath}/js/adminlte/bower_components/bootstrap/js/bootstrap.min.js"></script>
    <!-- FastClick -->
    <script src="${request.contextPath}/js/adminlte/bower_components/fastclick/fastclick.js"></script>
    <!-- AdminLTE App -->
    <script src="${request.contextPath}/js/adminlte/dist/js/adminlte.min.js"></script>
    <!-- jquery.slimscroll -->
    <script src="${request.contextPath}/js/adminlte/bower_components/jquery-slimscroll/jquery.slimscroll.min.js"></script>

    <!-- pace -->
    <script src="${request.contextPath}/js/adminlte/bower_components/PACE/pace.min.js"></script>
<#-- jquery cookie -->
    <script src="${request.contextPath}/js/plugins/jquery/jquery.cookie.js"></script>
<#-- jquery.validate -->
    <script src="${request.contextPath}/js/plugins/jquery/jquery.validate.min.js"></script>

<#-- layer -->
    <script src="${request.contextPath}/js/plugins/layer/layer.js"></script>

<#-- common -->
    <script src="${request.contextPath}/js/js/common.1.js"></script>
    <script>
        var base_url = '${request.contextPath}';
    </script>
</#macro>

<#macro commonFooter >
    <footer class="main-footer">
        Powered by <b>dragonchang</b>
        <div class="pull-right hidden-xs">
            <strong>Copyright &copy; 2015-${.now?string('yyyy')} &nbsp;
            </strong><!-- All rights reserved. -->
        </div>
    </footer>
</#macro>

<#macro commonStyle>

<#-- favicon -->
    <link rel="icon" href="${request.contextPath}/js/favicon.ico" />

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap -->
    <link rel="stylesheet" href="${request.contextPath}/js/adminlte/bower_components/bootstrap/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="${request.contextPath}/js/adminlte/bower_components/font-awesome/css/font-awesome.min.css">
    <!-- Ionicons -->
    <link rel="stylesheet" href="${request.contextPath}/js/adminlte/bower_components/Ionicons/css/ionicons.min.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="${request.contextPath}/js/adminlte/dist/css/AdminLTE.min.css">
    <!-- AdminLTE Skins. Choose a skin from the css/skins folder instead of downloading all of them to reduce the load. -->
    <link rel="stylesheet" href="${request.contextPath}/js/adminlte/dist/css/skins/_all-skins.min.css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

    <!-- pace -->
    <link rel="stylesheet" href="${request.contextPath}/js/adminlte/bower_components/PACE/themes/blue/pace-theme-flash.css">

</#macro>
