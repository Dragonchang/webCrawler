<!DOCTYPE html>
<html>
<head>
    <title>drip</title>
    <#import "./common/common.macro.ftl" as netCommon>
    <@netCommon.commonStyle />
    <!-- daterangepicker -->
    <link rel="stylesheet" href="${request.contextPath}/js/adminlte/bower_components/bootstrap-daterangepicker/daterangepicker.css">
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <!-- left -->
    <@netCommon.commonLeft />
    <div class="content-wrapper">
        <section class="content-header">
            <h1>推荐</h1>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="row">
                <section class="content-header">
                    <h1>每日新增上涨态势股票</h1>
                </section>
            </div>
            <div class="row">
                <div class="col-xs-2">
                    <div class="input-group">
                        <span class="input-group-addon">类型:</span>
                        <select class="form-control" id="newcondition1" >
                            <option value="1" >${"全部"}</option>
                            <option value="2" >${"非科创股"}</option>
                            <option value="3" >${"创业股"}</option>
                            <option value="4" >${"科创股"}</option>
                        </select>
                    </div>
                </div>
                <div class="col-xs-2">
                    <div class="input-group">
                        <span class="input-group-addon">名称</span>
                        <input type="text" class="form-control" id="name1" autocomplete="on" >
                    </div>
                </div>
                <div class="col-xs-2">
                    <div class="input-group">
                        <span class="input-group-addon">代码</span>
                        <input type="text" class="form-control" id="stock_code1" autocomplete="on" >
                    </div>
                </div>
                <!-- Date range -->
                <div class="col-xs-2">
                    <div class="input-group">
                        <span class="input-group-addon">时间：</span>
                        <div class="input-group-addon">
                            <i class="fa fa-calendar"></i>
                        </div>
                        <input type="text" class="form-control pull-right" id="reservation1">

                        <div class="input-group-addon clearBtns" id="clear1" >x</div>
                    </div>
                    <!-- /.input group -->
                </div>
                <div class="col-xs-1 pull-right">
                    <button class="btn btn-block btn-info " id="exportBtn1">导出</button>
                </div>
                <div class="col-xs-1 pull-right">
                    <button class="btn btn-block btn-info" id="searchBtn1">查询</button>
                </div>
            </div>

            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-body" >
                            <table id="upward_tend_list1" class="table table-bordered table-striped" width="100%" >
                                <thead>
                                <tr>
                                    <th name="id" >id</th>
                                    <th name="companyStockId" >关注公司ID</th>
                                    <th name="stockCode" >股票代码</th>
                                    <th name="name" >公司名称</th>
                                    <th name="lastPrice" >股票最新股价</th>
                                    <th name="reportTime" >生成时间</th>
                                    <th name="detail" >详情</th>
                                </tr>
                                </thead>
                                <tbody></tbody>
                                <tfoot></tfoot>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <section class="content-header">
                    <h1>每日所有上涨态势股票</h1>
                </section>
            </div>
            <div class="row">
                <div class="col-xs-2">
                    <div class="input-group">
                        <span class="input-group-addon">类型:</span>
                        <select class="form-control" id="condition1" >
                            <option value="1" >${"全部"}</option>
                            <option value="2" >${"非科创股"}</option>
                            <option value="3" >${"创业股"}</option>
                            <option value="4" >${"科创股"}</option>
                        </select>
                    </div>
                </div>
                <div class="col-xs-2">
                    <div class="input-group">
                        <span class="input-group-addon">跌破5日:</span>
                        <select class="form-control" id="condition2" >
                            <option value="1" >${"全部"}</option>
                            <option value="2" >${"否"}</option>
                            <option value="3" >${"是"}</option>
                        </select>
                    </div>
                </div>
                <div class="col-xs-2">
                    <div class="input-group">
                        <span class="input-group-addon">名称</span>
                        <input type="text" class="form-control" id="name" autocomplete="on" >
                    </div>
                </div>
                <div class="col-xs-2">
                    <div class="input-group">
                        <span class="input-group-addon">代码</span>
                        <input type="text" class="form-control" id="stock_code" autocomplete="on" >
                    </div>
                </div>
                <!-- Date range -->
                <div class="col-xs-2">
                    <div class="input-group">
                        <span class="input-group-addon">时间：</span>
                        <div class="input-group-addon">
                            <i class="fa fa-calendar"></i>
                        </div>
                        <input type="text" class="form-control pull-right" id="reservation">

                        <div class="input-group-addon clearBtns" id="clear" >x</div>
                    </div>
                    <!-- /.input group -->
                </div>
                <div class="col-xs-1 pull-right">
                    <button class="btn btn-block btn-info " id="exportBtn">导出</button>
                </div>
                <div class="col-xs-1 pull-right">
                    <button class="btn btn-block btn-info" id="searchBtn">查询</button>
                </div>
            </div>

            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-body" >
                            <table id="upward_tend_list" class="table table-bordered table-striped" width="100%" >
                                <thead>
                                <tr>
                                    <th name="id" >id</th>
                                    <th name="companyStockId" >关注公司ID</th>
                                    <th name="stockCode" >股票代码</th>
                                    <th name="name" >公司名称</th>
                                    <th name="lastPrice" >股票最新股价</th>
                                    <th name="avgFive" >五日</th>
                                    <th name="avgTen" >十日</th>
                                    <th name="avgTwenty" >二十日</th>
                                    <th name="avgThirty" >三十日</th>
                                    <th name="avgSixty" >六十日</th>
                                    <th name="avgNinety" >九十日</th>
                                    <th name="avgHundtwenty" >一百二十日</th>
                                    <th name="reportTime" >生成时间</th>
                                    <th name="detail" >详情</th>
                                </tr>
                                </thead>
                                <tbody></tbody>
                                <tfoot></tfoot>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
    <!-- footer -->
    <@netCommon.commonFooter />
</div>
<@netCommon.commonScript />
<!-- daterangepicker -->
<script src="${request.contextPath}/js/adminlte/bower_components/moment/moment.min.js"></script>
<script src="${request.contextPath}/js/adminlte/bower_components/bootstrap-daterangepicker/daterangepicker.js"></script>

<script src="${request.contextPath}/js/adminlte/bower_components/datatables.net/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/js/adminlte/bower_components/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
<script src="${request.contextPath}/js/upwardTrend.js"></script>
</body>
</html>