<?xml version="1.0" encoding="UTF-8"?>
<jmeterTestPlan version="1.2" properties="5.0" jmeter="5.0 r1840935">
    <hashTree>
        <TestPlan guiclass="TestPlanGui" testclass="TestPlan" testname="压测" enabled="true">
            <stringProp name="TestPlan.comments"></stringProp>
            <boolProp name="TestPlan.functional_mode">false</boolProp>
            <boolProp name="TestPlan.tearDown_on_shutdown">true</boolProp>
            <boolProp name="TestPlan.serialize_threadgroups">false</boolProp>
            <elementProp name="TestPlan.user_defined_variables" elementType="Arguments" guiclass="ArgumentsPanel" testclass="Arguments" testname="用户定义的变量" enabled="true">
                <collectionProp name="Arguments.arguments"/>
            </elementProp>
            <stringProp name="TestPlan.user_define_classpath"></stringProp>
        </TestPlan>
        <hashTree>
            <ThreadGroup guiclass="ThreadGroupGui" testclass="ThreadGroup" testname="线程组" enabled="true">
                <stringProp name="ThreadGroup.on_sample_error">continue</stringProp>
                <elementProp name="ThreadGroup.main_controller" elementType="LoopController" guiclass="LoopControlPanel" testclass="LoopController" testname="循环控制器" enabled="true">
                    <boolProp name="LoopController.continue_forever">false</boolProp>
                    <stringProp name="LoopController.loops">1</stringProp>
                </elementProp>
                <stringProp name="ThreadGroup.num_threads">1</stringProp>
                <stringProp name="ThreadGroup.ramp_time">1</stringProp>
                <boolProp name="ThreadGroup.scheduler">false</boolProp>
                <stringProp name="ThreadGroup.duration"></stringProp>
                <stringProp name="ThreadGroup.delay"></stringProp>
            </ThreadGroup>
            <hashTree>
                <Arguments guiclass="ArgumentsPanel" testclass="Arguments" testname="用户定义的变量" enabled="true">
                    <collectionProp name="Arguments.arguments">
                        <elementProp name="firstname" elementType="Argument">
                            <stringProp name="Argument.name">firstname</stringProp>
                            <stringProp name="Argument.value">ye</stringProp>
                            <stringProp name="Argument.metadata">=</stringProp>
                        </elementProp>
                        <elementProp name="lastname" elementType="Argument">
                            <stringProp name="Argument.name">lastname</stringProp>
                            <stringProp name="Argument.value">fei</stringProp>
                            <stringProp name="Argument.metadata">=</stringProp>
                        </elementProp>
                        <elementProp name="age" elementType="Argument">
                            <stringProp name="Argument.name">age</stringProp>
                            <stringProp name="Argument.value">18</stringProp>
                            <stringProp name="Argument.metadata">=</stringProp>
                        </elementProp>
                    </collectionProp>
                </Arguments>
                <hashTree/>
                <HeaderManager guiclass="HeaderPanel" testclass="HeaderManager" testname="HTTP信息头管理器" enabled="true">
                    <collectionProp name="HeaderManager.headers">
                        <elementProp name="" elementType="Header">
                            <stringProp name="Header.name">Content-Type</stringProp>
                            <stringProp name="Header.value">application/x-www-form-urlencoded</stringProp>
                        </elementProp>
                    </collectionProp>
                </HeaderManager>
                <hashTree/>
                <HTTPSamplerProxy guiclass="HttpTestSampleGui" testclass="HTTPSamplerProxy" testname="HTTP请求" enabled="true">
                    <boolProp name="HTTPSampler.postBodyRaw">true</boolProp>
                    <elementProp name="HTTPsampler.Arguments" elementType="Arguments">
                        <collectionProp name="Arguments.arguments">
                            <elementProp name="" elementType="HTTPArgument">
                                <boolProp name="HTTPArgument.always_encode">false</boolProp>
                                <stringProp name="Argument.value">firstname=${firstname}&amp;lastname=${lastname}&amp;age=${age}</stringProp>
                                <stringProp name="Argument.metadata">=</stringProp>
                            </elementProp>
                        </collectionProp>
                    </elementProp>
                    <stringProp name="HTTPSampler.domain">pre.rest.mock.tp.hualala.com</stringProp>
                    <stringProp name="HTTPSampler.port"></stringProp>
                    <stringProp name="HTTPSampler.protocol">http</stringProp>
                    <stringProp name="HTTPSampler.contentEncoding"></stringProp>
                    <stringProp name="HTTPSampler.path">/test/script</stringProp>
                    <stringProp name="HTTPSampler.method">POST</stringProp>
                    <boolProp name="HTTPSampler.follow_redirects">true</boolProp>
                    <boolProp name="HTTPSampler.auto_redirects">false</boolProp>
                    <boolProp name="HTTPSampler.use_keepalive">true</boolProp>
                    <boolProp name="HTTPSampler.DO_MULTIPART_POST">false</boolProp>
                    <stringProp name="HTTPSampler.embedded_url_re"></stringProp>
                    <stringProp name="HTTPSampler.connect_timeout"></stringProp>
                    <stringProp name="HTTPSampler.response_timeout"></stringProp>
                </HTTPSamplerProxy>
                <hashTree>
                    <JSONPostProcessor guiclass="JSONPostProcessorGui" testclass="JSONPostProcessor" testname="JSON提取器" enabled="true">
                        <stringProp name="JSONPostProcessor.referenceNames">username</stringProp>
                        <stringProp name="JSONPostProcessor.jsonPathExprs">$.userinfo.username</stringProp>
                        <stringProp name="JSONPostProcessor.match_numbers">1</stringProp>
                    </JSONPostProcessor>
                    <hashTree/>
                    <JSONPathAssertion guiclass="JSONPathAssertionGui" testclass="JSONPathAssertion" testname="JSON断言" enabled="true">
                        <stringProp name="JSON_PATH">$.code</stringProp>
                        <stringProp name="EXPECTED_VALUE">200</stringProp>
                        <boolProp name="JSONVALIDATION">true</boolProp>
                        <boolProp name="EXPECT_NULL">false</boolProp>
                        <boolProp name="INVERT">false</boolProp>
                        <boolProp name="ISREGEX">false</boolProp>
                    </JSONPathAssertion>
                    <hashTree/>
                </hashTree>
                <HTTPSamplerProxy guiclass="HttpTestSampleGui" testclass="HTTPSamplerProxy" testname="HTTP请求" enabled="true">
                    <boolProp name="HTTPSampler.postBodyRaw">true</boolProp>
                    <elementProp name="HTTPsampler.Arguments" elementType="Arguments">
                        <collectionProp name="Arguments.arguments">
                            <elementProp name="" elementType="HTTPArgument">
                                <boolProp name="HTTPArgument.always_encode">false</boolProp>
                                <stringProp name="Argument.value">firstname=${firstname}&amp;lastname=${lastname}&amp;age=${age}&amp;username=${username}</stringProp>
                                <stringProp name="Argument.metadata">=</stringProp>
                            </elementProp>
                        </collectionProp>
                    </elementProp>
                    <stringProp name="HTTPSampler.domain">pre.rest.mock.tp.hualala.com</stringProp>
                    <stringProp name="HTTPSampler.port"></stringProp>
                    <stringProp name="HTTPSampler.protocol">http</stringProp>
                    <stringProp name="HTTPSampler.contentEncoding"></stringProp>
                    <stringProp name="HTTPSampler.path">/test/script</stringProp>
                    <stringProp name="HTTPSampler.method">POST</stringProp>
                    <boolProp name="HTTPSampler.follow_redirects">true</boolProp>
                    <boolProp name="HTTPSampler.auto_redirects">false</boolProp>
                    <boolProp name="HTTPSampler.use_keepalive">true</boolProp>
                    <boolProp name="HTTPSampler.DO_MULTIPART_POST">false</boolProp>
                    <stringProp name="HTTPSampler.embedded_url_re"></stringProp>
                    <stringProp name="HTTPSampler.connect_timeout"></stringProp>
                    <stringProp name="HTTPSampler.response_timeout"></stringProp>
                </HTTPSamplerProxy>
                <hashTree/>
                <JavaSampler guiclass="JavaTestSamplerGui" testclass="JavaSampler" testname="Java请求" enabled="true">
                    <elementProp name="arguments" elementType="Arguments" guiclass="ArgumentsPanel" testclass="Arguments" enabled="true">
                        <collectionProp name="Arguments.arguments">
                            <elementProp name="target" elementType="Argument">
                                <stringProp name="Argument.name">target</stringProp>
                                <stringProp name="Argument.value">pre.order.service.hualala.com:31515</stringProp>
                                <stringProp name="Argument.metadata">=</stringProp>
                            </elementProp>
                            <elementProp name="serviceName" elementType="Argument">
                                <stringProp name="Argument.name">serviceName</stringProp>
                                <stringProp name="Argument.value">proto.msg.SaasOrderService</stringProp>
                                <stringProp name="Argument.metadata">=</stringProp>
                            </elementProp>
                            <elementProp name="methodName" elementType="Argument">
                                <stringProp name="Argument.name">methodName</stringProp>
                                <stringProp name="Argument.value">submitOrder</stringProp>
                                <stringProp name="Argument.metadata">=</stringProp>
                            </elementProp>
                            <elementProp name="param" elementType="Argument">
                                <stringProp name="Argument.name">param</stringProp>
                                <stringProp name="Argument.value">{&quot;header&quot;: {&quot;traceID&quot;: &quot;1539078002142&quot;},&quot;order&quot;: {&quot;groupID&quot;: 121955,&quot;shopID&quot;: 76160708,&quot;paidTotalMoney&quot;: 26.0,&quot;shopName&quot;: &quot;喜记抓面&quot;,&quot;clientType&quot;: &quot;55&quot;,&quot;shopOrderKey&quot;: &quot;2018100917395613736017240049&quot;,&quot;orderSubType&quot;: &quot;SUB_BUFFET&quot;,&quot;QRTypeEnum&quot;: &quot;WECHATPAY&quot;,&quot;scanType&quot;: &quot;QR_SCAN&quot;,&quot;paymentCheckCode&quot;: &quot;174002&quot;,&quot;orderItemList&quot;: [{&quot;foodCategoryName&quot;: &quot;喜记抓面&quot;,&quot;unit&quot;: &quot;碗&quot;,&quot;number&quot;: &quot;1.0000&quot;,&quot;payPrice&quot;: &quot;13.00&quot;,&quot;payPriceMember&quot;: &quot;13.00&quot;,&quot;foodName&quot;: &quot;花面（大）&quot;,&quot;isDiscount&quot;: &quot;1&quot;,&quot;isWaitConfirmNumber&quot;: &quot;0&quot;,&quot;unitKey&quot;: &quot;164035447&quot;,&quot;unitCode&quot;: &quot;164035447&quot;,&quot;foodCode&quot;: &quot;0109&quot;,&quot;payTotal&quot;: &quot;0.00&quot;,&quot;sequenceID&quot;: &quot;c4727605-6e3c-4d69-b5b0-b6b2e99678f0&quot;},{&quot;foodCategoryName&quot;: &quot;喜记抓面&quot;,&quot;unit&quot;: &quot;碗&quot;,&quot;number&quot;: &quot;1.0000&quot;,&quot;payPrice&quot;: &quot;13.00&quot;,&quot;payPriceMember&quot;: &quot;13.00&quot;,&quot;foodName&quot;: &quot;肉面（大）&quot;,&quot;isDiscount&quot;: &quot;1&quot;,&quot;isWaitConfirmNumber&quot;: &quot;0&quot;,&quot;unitKey&quot;: &quot;164035252&quot;,&quot;unitCode&quot;: &quot;164035252&quot;,&quot;foodCode&quot;: &quot;0107&quot;,&quot;payTotal&quot;: &quot;0.00&quot;,&quot;sequenceID&quot;: &quot;7b2e65d2-9875-4d7a-a0a1-65c9e3217a2f&quot;}],&quot;groupName&quot;: &quot;青海尼罗清真美食城&quot;,&quot;originTotalMoney&quot;: 26.0,&quot;paidAmount&quot;: &quot;0&quot;,&quot;moneyWipeZeroType&quot;: &quot;3&quot;,&quot;receivableAmount&quot;: 26.0}}</stringProp>
                                <stringProp name="Argument.metadata">=</stringProp>
                            </elementProp>
                            <elementProp name="serviceAccessToken" elementType="Argument">
                                <stringProp name="Argument.name">serviceAccessToken</stringProp>
                                <stringProp name="Argument.value"></stringProp>
                                <stringProp name="Argument.metadata">=</stringProp>
                            </elementProp>
                            <elementProp name="shopID" elementType="Argument">
                                <stringProp name="Argument.name">shopID</stringProp>
                                <stringProp name="Argument.value"></stringProp>
                                <stringProp name="Argument.metadata">=</stringProp>
                            </elementProp>
                            <elementProp name="groupID" elementType="Argument">
                                <stringProp name="Argument.name">groupID</stringProp>
                                <stringProp name="Argument.value"></stringProp>
                                <stringProp name="Argument.metadata">=</stringProp>
                            </elementProp>
                        </collectionProp>
                    </elementProp>
                    <stringProp name="classname">com.hualala.qa.jmeter.plugin.grpc.sample.GrpcSample</stringProp>
                </JavaSampler>
                <hashTree>
                    <JSONPathAssertion guiclass="JSONPathAssertionGui" testclass="JSONPathAssertion" testname="JSON断言" enabled="true">
                        <stringProp name="JSON_PATH">$.result.code</stringProp>
                        <stringProp name="EXPECTED_VALUE">0000</stringProp>
                        <boolProp name="JSONVALIDATION">true</boolProp>
                        <boolProp name="EXPECT_NULL">false</boolProp>
                        <boolProp name="INVERT">false</boolProp>
                        <boolProp name="ISREGEX">true</boolProp>
                    </JSONPathAssertion>
                    <hashTree/>
                    <JSONPostProcessor guiclass="JSONPostProcessorGui" testclass="JSONPostProcessor" testname="JSON提取器" enabled="true">
                        <stringProp name="JSONPostProcessor.referenceNames">orderKey</stringProp>
                        <stringProp name="JSONPostProcessor.jsonPathExprs">$.orderKey</stringProp>
                        <stringProp name="JSONPostProcessor.match_numbers"></stringProp>
                    </JSONPostProcessor>
                    <hashTree/>
                </hashTree>
                <ResultSaver guiclass="ResultSaverGui" testclass="ResultSaver" testname="保存响应到文件" enabled="true">
                    <stringProp name="FileSaver.filename">/tmp/jmeter-result/status-</stringProp>
                    <boolProp name="FileSaver.errorsonly">true</boolProp>
                    <boolProp name="FileSaver.successonly">false</boolProp>
                    <boolProp name="FileSaver.skipsuffix">false</boolProp>
                    <boolProp name="FileSaver.skipautonumber">false</boolProp>
                    <boolProp name="FileSaver.addTimstamp">true</boolProp>
                </ResultSaver>
                <hashTree/>
                <ResultCollector guiclass="ViewResultsFullVisualizer" testclass="ResultCollector" testname="察看结果树" enabled="true">
                    <boolProp name="ResultCollector.error_logging">false</boolProp>
                    <objProp>
                        <name>saveConfig</name>
                        <value class="SampleSaveConfiguration">
                            <time>true</time>
                            <latency>true</latency>
                            <timestamp>true</timestamp>
                            <success>true</success>
                            <label>true</label>
                            <code>true</code>
                            <message>true</message>
                            <threadName>true</threadName>
                            <dataType>true</dataType>
                            <encoding>false</encoding>
                            <assertions>true</assertions>
                            <subresults>true</subresults>
                            <responseData>false</responseData>
                            <samplerData>false</samplerData>
                            <xml>false</xml>
                            <fieldNames>true</fieldNames>
                            <responseHeaders>false</responseHeaders>
                            <requestHeaders>false</requestHeaders>
                            <responseDataOnError>false</responseDataOnError>
                            <saveAssertionResultsFailureMessage>true</saveAssertionResultsFailureMessage>
                            <assertionsResultsToSave>0</assertionsResultsToSave>
                            <bytes>true</bytes>
                            <sentBytes>true</sentBytes>
                            <url>true</url>
                            <threadCounts>true</threadCounts>
                            <idleTime>true</idleTime>
                            <connectTime>true</connectTime>
                        </value>
                    </objProp>
                    <stringProp name="filename"></stringProp>
                </ResultCollector>
                <hashTree/>
            </hashTree>
        </hashTree>
    </hashTree>
</jmeterTestPlan>
