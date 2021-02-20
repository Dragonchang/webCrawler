<#macro commonLeft>
    <!-- Main Sidebar Container -->
    <aside class="main-sidebar sidebar-dark-primary elevation-4">
        <!-- Brand Logo -->
        <a href="/" class="brand-link">
            <img src="${request.contextPath}/dist/img/AdminLTELogo.png" alt="AdminLTE Logo" class="brand-image img-circle elevation-3" style="opacity: .8">
            <span class="brand-text font-weight-light">crawler</span>
        </a>

        <!-- Sidebar -->
        <div class="sidebar">
            <!-- Sidebar Menu -->
            <nav class="mt-2">
                <ul class="nav nav-pills nav-sidebar flex-column" data-widget="treeview" role="menu" data-accordion="false">
                    <!-- Add icons to the links using the .nav-icon class
                         with font-awesome or any other icon font library -->
                    <li class="nav-item">
                        <a href="/" class="nav-link">
                            <i class="nav-icon fas fa-th"></i>
                            <p>
                                Dashboard
                            </p>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="${request.contextPath}/company" class="nav-link">
                            <i class="nav-icon fas fa-th"></i>
                            <p>
                                Company
                            </p>
                        </a>
                    </li>
                </ul>
            </nav>
            <!-- /.sidebar-menu -->
        </div>
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
