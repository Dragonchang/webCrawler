package com.dragonchang.tianyancha;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @Author: chenyanfeng
 * @Date: 2019-08-14
 * @Time: 下午3:05
 */
public class HeaderUtils {

    public static Map<String, String> getTYCWebNameHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "TYCID=999864106f7511ebb9200957383abf78; ssuid=3820820752; _ga=GA1.2.608211513.1613383653; CLOUDID=2abc95cf-b1d6-4e72-aba0-e1361add125f; CT_TYCID=4083810bf24b429082fba69a6874d978; tyc-user-info={%22claimEditPoint%22:%220%22%2C%22vipToMonth%22:%22false%22%2C%22explainPoint%22:%220%22%2C%22personalClaimType%22:%22none%22%2C%22integrity%22:%2210%25%22%2C%22state%22:%220%22%2C%22score%22:%2248%22%2C%22announcementPoint%22:%220%22%2C%22messageShowRedPoint%22:%220%22%2C%22bidSubscribe%22:%22-1%22%2C%22vipManager%22:%220%22%2C%22monitorUnreadCount%22:%220%22%2C%22discussCommendCount%22:%220%22%2C%22onum%22:%220%22%2C%22showPost%22:null%2C%22messageBubbleCount%22:%220%22%2C%22claimPoint%22:%220%22%2C%22token%22:%22eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMzk1MTYyNjk0OSIsImlhdCI6MTYxMzM4Mzg4OSwiZXhwIjoxNjQ0OTE5ODg5fQ.PNMjiXwW7v0RodnjPjflaULeqQ57vPc6-j85HeyL-07CfJxDTa4KlErdBtZAylVW-hXEprf4badUUsYmAg0W7Q%22%2C%22schoolAuthStatus%22:%222%22%2C%22userId%22:%22234231069%22%2C%22scoreUnit%22:%22%22%2C%22redPoint%22:%220%22%2C%22myTidings%22:%220%22%2C%22companyAuthStatus%22:%222%22%2C%22originalScore%22:%2248%22%2C%22myAnswerCount%22:%220%22%2C%22myQuestionCount%22:%220%22%2C%22signUp%22:%220%22%2C%22privateMessagePointWeb%22:%220%22%2C%22nickname%22:%22%E5%8B%87%E5%BA%A6%22%2C%22privateMessagePoint%22:%220%22%2C%22bossStatus%22:%222%22%2C%22isClaim%22:%220%22%2C%22yellowDiamondEndTime%22:%220%22%2C%22yellowDiamondStatus%22:%22-1%22%2C%22pleaseAnswerCount%22:%220%22%2C%22bizCardUnread%22:%220%22%2C%22vnum%22:%220%22%2C%22mobile%22:%2213951626949%22%2C%22riskManagement%22:{%22servicePhone%22:null%2C%22mobile%22:13951626949%2C%22title%22:null%2C%22currentStatus%22:null%2C%22lastStatus%22:null%2C%22quickReturn%22:false%2C%22oldVersionMessage%22:null%2C%22riskMessage%22:null}}; auth_token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMzk1MTYyNjk0OSIsImlhdCI6MTYxMzM4Mzg4OSwiZXhwIjoxNjQ0OTE5ODg5fQ.PNMjiXwW7v0RodnjPjflaULeqQ57vPc6-j85HeyL-07CfJxDTa4KlErdBtZAylVW-hXEprf4badUUsYmAg0W7Q; tyc-user-info-save-time=1613383895599; tyc-user-phone=%255B%252213951626949%2522%255D; searchSessionId=1613383925.12322553; RTYCID=4fc574d5141142d58be576d6acdac55c; jsid=SEO-BAIDU-ALL-SY-000001; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%22234231069%22%2C%22first_id%22%3A%22177a528d27df4-0b29d5d54e85fd-3323765-921600-177a528d27e6cd%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E8%87%AA%E7%84%B6%E6%90%9C%E7%B4%A2%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC%22%2C%22%24latest_referrer%22%3A%22https%3A%2F%2Fwww.baidu.com%2Flink%22%7D%2C%22%24device_id%22%3A%22177a528d27df4-0b29d5d54e85fd-3323765-921600-177a528d27e6cd%22%7D; bannerFlag=true; Hm_lvt_e92c8d65d92d534b0fc290df538b4758=1613383653,1613532127; _gid=GA1.2.284833558.1613532128; aliyungf_tc=25c74c302d243d7240de6181fe45e970b065381eeba8e8a7281a66aea6dc5f5e; bad_id658cce70-d9dc-11e9-96c6-833900356dc6=3d63f561-70d8-11eb-9058-bf16dedb738d; nice_id658cce70-d9dc-11e9-96c6-833900356dc6=3d63f562-70d8-11eb-9058-bf16dedb738d; Hm_lpvt_e92c8d65d92d534b0fc290df538b4758=1613537890; cloud_token=d7ec04b2b1b34fbfb6a864c6c259c29e; cloud_utm=d71b38352f9b409b8c9cafc68bf1c8fe");
        headers.put("Host", "capi.tianyancha.com");
        headers.put("Origin", "https://dis.tianyancha.com");
        headers.put("Referer", "https://dis.tianyancha.com/dis/tree?cid=3484794127&origin=https%3A%2F%2Fwww.tianyancha.com&mobile=13951626949&time=1613532207521bf43&full=1");
        headers.put("version", "TYC-Web");
        headers.put("Content-Type", "application/json");
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("Accept", "application/json, text/plain, */*");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        return headers;
    }

    public static Map<String, String> getTYCWebIndexNodeHeaders(String cloud_token, String cloud_utm) {
        String cookie = "TYCID=999864106f7511ebb9200957383abf78; ssuid=3820820752; _ga=GA1.2.608211513.1613383653; CLOUDID=2abc95cf-b1d6-4e72-aba0-e1361add125f; CT_TYCID=4083810bf24b429082fba69a6874d978; tyc-user-info={%22claimEditPoint%22:%220%22%2C%22vipToMonth%22:%22false%22%2C%22explainPoint%22:%220%22%2C%22personalClaimType%22:%22none%22%2C%22integrity%22:%2210%25%22%2C%22state%22:%220%22%2C%22score%22:%2248%22%2C%22announcementPoint%22:%220%22%2C%22messageShowRedPoint%22:%220%22%2C%22bidSubscribe%22:%22-1%22%2C%22vipManager%22:%220%22%2C%22monitorUnreadCount%22:%220%22%2C%22discussCommendCount%22:%220%22%2C%22onum%22:%220%22%2C%22showPost%22:null%2C%22messageBubbleCount%22:%220%22%2C%22claimPoint%22:%220%22%2C%22token%22:%22eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMzk1MTYyNjk0OSIsImlhdCI6MTYxMzM4Mzg4OSwiZXhwIjoxNjQ0OTE5ODg5fQ.PNMjiXwW7v0RodnjPjflaULeqQ57vPc6-j85HeyL-07CfJxDTa4KlErdBtZAylVW-hXEprf4badUUsYmAg0W7Q%22%2C%22schoolAuthStatus%22:%222%22%2C%22userId%22:%22234231069%22%2C%22scoreUnit%22:%22%22%2C%22redPoint%22:%220%22%2C%22myTidings%22:%220%22%2C%22companyAuthStatus%22:%222%22%2C%22originalScore%22:%2248%22%2C%22myAnswerCount%22:%220%22%2C%22myQuestionCount%22:%220%22%2C%22signUp%22:%220%22%2C%22privateMessagePointWeb%22:%220%22%2C%22nickname%22:%22%E5%8B%87%E5%BA%A6%22%2C%22privateMessagePoint%22:%220%22%2C%22bossStatus%22:%222%22%2C%22isClaim%22:%220%22%2C%22yellowDiamondEndTime%22:%220%22%2C%22yellowDiamondStatus%22:%22-1%22%2C%22pleaseAnswerCount%22:%220%22%2C%22bizCardUnread%22:%220%22%2C%22vnum%22:%220%22%2C%22mobile%22:%2213951626949%22%2C%22riskManagement%22:{%22servicePhone%22:null%2C%22mobile%22:13951626949%2C%22title%22:null%2C%22currentStatus%22:null%2C%22lastStatus%22:null%2C%22quickReturn%22:false%2C%22oldVersionMessage%22:null%2C%22riskMessage%22:null}}; auth_token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMzk1MTYyNjk0OSIsImlhdCI6MTYxMzM4Mzg4OSwiZXhwIjoxNjQ0OTE5ODg5fQ.PNMjiXwW7v0RodnjPjflaULeqQ57vPc6-j85HeyL-07CfJxDTa4KlErdBtZAylVW-hXEprf4badUUsYmAg0W7Q; tyc-user-info-save-time=1613383895599; tyc-user-phone=%255B%252213951626949%2522%255D; searchSessionId=1613383925.12322553; RTYCID=4fc574d5141142d58be576d6acdac55c; jsid=SEO-BAIDU-ALL-SY-000001; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%22234231069%22%2C%22first_id%22%3A%22177a528d27df4-0b29d5d54e85fd-3323765-921600-177a528d27e6cd%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E8%87%AA%E7%84%B6%E6%90%9C%E7%B4%A2%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC%22%2C%22%24latest_referrer%22%3A%22https%3A%2F%2Fwww.baidu.com%2Flink%22%7D%2C%22%24device_id%22%3A%22177a528d27df4-0b29d5d54e85fd-3323765-921600-177a528d27e6cd%22%7D; Hm_lvt_e92c8d65d92d534b0fc290df538b4758=1613383653,1613532127; _gid=GA1.2.284833558.1613532128; bad_id658cce70-d9dc-11e9-96c6-833900356dc6=3d63f561-70d8-11eb-9058-bf16dedb738d; aliyungf_tc=0544427c6cabec159782609827911dc04a0e1b7ee269e13474b717682e8189c2; ";
        cookie = cookie + cloud_token + "; cloud_utm=" + cloud_utm + ";";
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "capi.tianyancha.com");
        headers.put("Accept", "application/json, text/plain, */*");
        headers.put("X-AUTH-TOKEN", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMzk1MTYyNjk0OSIsImlhdCI6MTYxMzM4Mzg4OSwiZXhwIjoxNjQ0OTE5ODg5fQ.PNMjiXwW7v0RodnjPjflaULeqQ57vPc6-j85HeyL-07CfJxDTa4KlErdBtZAylVW-hXEprf4badUUsYmAg0W7Q");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36");
        headers.put("version", "TYC-Web");
        headers.put("Origin", "https://dis.tianyancha.com");
        headers.put("Referer", "https://dis.tianyancha.com/dis/tree?cid=3484794127&origin=https%3A%2F%2Fwww.tianyancha.com&mobile=13951626949&time=1613532207521bf43&full=1");
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        headers.put("Cookie", cookie);
        return headers;
    }

    public static Map<String, String> getEastStockListWebHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "qgqp_b_id=3159c429a9f9980500703d5341bea318; mtp=1; ct=2_jL3QZBMIX6XzDQmk3alBF0hN51hZpf22WpVYFBCzobOP2Xm-VVhkIIuUq7FJZgw8EYvFT7zkZZh4vagfokzSeAh6SX1-EHgIO5TTmFzAKXUW8-AbzINyu2iiQHKL13gDRZ6DXpJfUrOODcpfUtCVuFbL3Xlyp-c0AkXz-KZvI; ut=FobyicMgeV5kZz00Ot3UbweF9-4Uhjwa6xNkABS5WUWGRd5dlSR49ZDFPsYieq4r1sTmIWpizEGBLtINqyn_YcCAw348Nw2n99IEUdQfqVENbz2bthjsDMmjn94cxN8xaZOH-yZ1wxmJe1ljijpNfdqcHuDF0cSkwxCkO3qCNRJpK79XzuW7LuKjtzBwCuw9WCpJEC-WTMax_4Ga6QgT_veWxXUxY-1uPL3r8K7efHwis8E0X0tl36GdNNatyN9OdqWy_MwYSyf57hr5wpfDXg; pi=3914356125019460%3Bu3914356125019460%3B%E8%82%A1%E5%8F%8B189d763T18%3BgjSIQrPC5LukdtUgiWaWop85NsKcpw8voKeP9LcA7xnj4Gs5Dam8admVCQu5W%2B3YqVgtGeTzG%2BwftUIlrfoh1T95Wovkm%2F5%2BZnR08FziZmMQpGYN8pCaDZ53%2F9LpQyfWkW0C8ddn7%2Fl6KM3g7STdOtrwxyDysJXnRA52u1g4mMhgxf62iFra5abP1gQOnAVqnbf4Tn%2Bj%3BEDoCSbjaGaz0H%2B%2BEaY3HuX7zhhUDJ3qajlBuAV6rDegm3RpD%2F%2F8Q%2Fl5nPyOXQFQblmKTJxqi58KbVOBtk4ocTllWUz5L%2FEEAAzvn626F8CYuZ%2Fiz4dMEbPKTEmbU%2FD39jnX%2BHUUkFij9AY27a%2BgZJ8xa6L09pQ%3D%3D; uidal=3914356125019460%e8%82%a1%e5%8f%8b189d763T18; sid=159759623; vtpst=|; st_si=42038180615794; st_asi=delete; HAList=ty-0-873593-%u9F0E%u667A%u79D1%u6280%2Cty-0-300059-%u4E1C%u65B9%u8D22%u5BCC%2Cty-107-PIFI-ClearShares%20Piton%20Intermediate%20%2Cty-90-BK1141-%u6DF7%u5408%u73B0%u5B9E%2Cty-90-BK1134-%u7B97%u529B%u6982%u5FF5%2Cty-90-BK1153-%u591A%u6A21%u6001AI%2Cty-90-BK1015-%u80FD%u6E90%u91D1%u5C5E%2Cty-90-BK0485-%u65C5%u6E38%u9152%u5E97%2Cty-90-BK1039-%u7535%u5B50%u5316%u5B66%u54C1%2Cty-90-BK0732-%u8D35%u91D1%u5C5E; st_pvi=68340009970843; st_sp=2023-06-05%2015%3A15%3A46; st_inirUrl=https%3A%2F%2Fwww.google.com%2F; st_sn=95; st_psi=2024010418253239-113200301321-7972529669");
        headers.put("Host", "88.push2.eastmoney.com");
        headers.put("Referer", "http://quote.eastmoney.com/center/gridlist.html");
        //headers.put("Content-Type", "application/json");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Accept", "*/*");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
        return headers;
    }

    public static Map<String, String> getEastMoneyWebHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "em_hq_fls=js; intellpositionL=1098.55px; cowCookie=true; qgqp_b_id=6b3919ddd2627ee0b79a811b768ed9c7; st_si=38158891947471; waptgshowtime=2021223; ct=rWrV5tp0cim5qdboh2dznhmXG633Y9tEuryPCu3cNdNBeZV3VIBDSWYZpZBUAJqfEfOoqIhqTvLCq2985qr7JzTE5AMVu_TFRohdFzroNMCVfvxYcyVdQ7JOd2ySiZ6OCsa2G159yzjmrPW3XHWqdazFjL9K6Hu1iNlz6v8-_eo; ut=FobyicMgeV54OLFNgnrRkxIk0Th1XtPFM_6m4ChA38ixLvuYNpEeU8DKSYF-CS4ozMuBKq6ZkP9Tfpe_EJwfTuwm1Q1xE3iJyd2zfAchVGKWBiV-gUn8DmRP7kacmavO53irf6rFPCqYTBXzDbLB0f_DINjMrH8BRicYN3CS8MdqhktPwZOgPh9Tp5mSplRoGMhqBQiAPweT-wPWZTliOBuVzk9EhvXD73wh03ffN3_QFZNCQ7LoMI9YgsDlB3B9z9-l5599yzE; pi=3914356125019460%3bu3914356125019460%3b%e8%82%a1%e5%8f%8b189d763T18%3b5R4UtE1Sq45uEc47dK70qD4OFgXsyqFmZyagv8yPiJOE%2f50r9sI4hSmXKsctXZrsfeOtxgF6F6f1wBV8hhSPt7t881duAU%2fKAGTP6m0%2bb3Oks4lJoeXgwLQoXVF6eqKm1kKZmxHf2GmUkxu9wGb3NqXPYdLFWu43cPcM7hlgKqLGUC8z8ODY3RAg%2fDcFLgTMNiQvk5ei%3bkYSp%2bhVzJFgozBdqquyA9awAVkkl58vo4FkAl3VfnLppjWRULAA9bcYM58sVfDMKCiY2y060uBmoG0OaIXeEMYAEfcXTCwOguoiKHlNqkKhaaG6e0GyJZJNueYUiWAY2p3cZEdc0JObhCSALO3N1YsuGA2xCPA%3d%3d; uidal=3914356125019460%e8%82%a1%e5%8f%8b189d763T18; sid=159759623; vtpst=|; emshistory=%5B%22%E9%A6%99%E6%B8%AF%E4%B8%AD%E5%A4%AE%E7%BB%93%E7%AE%97%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8%22%5D; HAList=a-sh-600519-%u8D35%u5DDE%u8305%u53F0%2Ca-sh-603893-%u745E%u82AF%u5FAE; yzmkey=8df120183ec24c1abf421e300a0b09f0; st_asi=delete; intellpositionT=546.304px; st_pvi=70086857373304; st_sp=2020-07-20%2016%3A35%3A41; st_inirUrl=https%3A%2F%2Fwww.baidu.com%2Flink; st_sn=33; st_psi=20210223141958826-113200301321-7980126401");
        headers.put("Host", "39.push2.eastmoney.com");
        headers.put("Referer", "http://quote.eastmoney.com/");
        //headers.put("Content-Type", "application/json");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Accept", "*/*");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
        return headers;
    }

    public static Map<String, String> getEastMoneyHolderDataHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept-Encoding", "gzip, deflate, br, zstd");
        headers.put("Accept", "*/*");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
        headers.put("Connection", "keep-alive");
        headers.put("Host", "datacenter.eastmoney.com");
        headers.put("Origin", "https://emweb.securities.eastmoney.com");
        headers.put("Referer", "https://emweb.securities.eastmoney.com/");
        headers.put("Sec-Ch-Ua", "Chromium\";v=\"124\", \"Microsoft Edge\";v=\"124\", \"Not-A.Brand\";v=\"99");
        headers.put("Sec-Ch-Ua-Mobile", "?0");
        headers.put("Sec-Ch-Ua-Platform", "Windows");
        headers.put("Sec-Fetch-Dest", "empty");
        headers.put("Sec-Fetch-Mode", "cors");
        headers.put("Sec-Fetch-Site", "same-site");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36 Edg/124.0.0.0");
        return headers;
    }

    public static Map<String, String> getEastMoneyHolderHeaders(String code) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "em_hq_fls=js; intellpositionL=1098.55px; cowCookie=true; qgqp_b_id=6b3919ddd2627ee0b79a811b768ed9c7; st_si=38158891947471; waptgshowtime=2021223; ct=rWrV5tp0cim5qdboh2dznhmXG633Y9tEuryPCu3cNdNBeZV3VIBDSWYZpZBUAJqfEfOoqIhqTvLCq2985qr7JzTE5AMVu_TFRohdFzroNMCVfvxYcyVdQ7JOd2ySiZ6OCsa2G159yzjmrPW3XHWqdazFjL9K6Hu1iNlz6v8-_eo; ut=FobyicMgeV54OLFNgnrRkxIk0Th1XtPFM_6m4ChA38ixLvuYNpEeU8DKSYF-CS4ozMuBKq6ZkP9Tfpe_EJwfTuwm1Q1xE3iJyd2zfAchVGKWBiV-gUn8DmRP7kacmavO53irf6rFPCqYTBXzDbLB0f_DINjMrH8BRicYN3CS8MdqhktPwZOgPh9Tp5mSplRoGMhqBQiAPweT-wPWZTliOBuVzk9EhvXD73wh03ffN3_QFZNCQ7LoMI9YgsDlB3B9z9-l5599yzE; pi=3914356125019460%3bu3914356125019460%3b%e8%82%a1%e5%8f%8b189d763T18%3b5R4UtE1Sq45uEc47dK70qD4OFgXsyqFmZyagv8yPiJOE%2f50r9sI4hSmXKsctXZrsfeOtxgF6F6f1wBV8hhSPt7t881duAU%2fKAGTP6m0%2bb3Oks4lJoeXgwLQoXVF6eqKm1kKZmxHf2GmUkxu9wGb3NqXPYdLFWu43cPcM7hlgKqLGUC8z8ODY3RAg%2fDcFLgTMNiQvk5ei%3bkYSp%2bhVzJFgozBdqquyA9awAVkkl58vo4FkAl3VfnLppjWRULAA9bcYM58sVfDMKCiY2y060uBmoG0OaIXeEMYAEfcXTCwOguoiKHlNqkKhaaG6e0GyJZJNueYUiWAY2p3cZEdc0JObhCSALO3N1YsuGA2xCPA%3d%3d; uidal=3914356125019460%e8%82%a1%e5%8f%8b189d763T18; sid=159759623; vtpst=|; emshistory=%5B%22%E9%A6%99%E6%B8%AF%E4%B8%AD%E5%A4%AE%E7%BB%93%E7%AE%97%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8%22%5D; HAList=a-sh-600519-%u8D35%u5DDE%u8305%u53F0%2Ca-sh-603893-%u745E%u82AF%u5FAE; yzmkey=8df120183ec24c1abf421e300a0b09f0; st_asi=delete; intellpositionT=546.304px; st_pvi=70086857373304; st_sp=2020-07-20%2016%3A35%3A41; st_inirUrl=https%3A%2F%2Fwww.baidu.com%2Flink; st_sn=33; st_psi=20210223141958826-113200301321-7980126401");
        headers.put("Host", "f10.eastmoney.com");
        headers.put("Referer", "http://f10.eastmoney.com/ShareholderResearch/Index?type=web&code="+code);
        headers.put("X-Requested-With", "XMLHttpRequest");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Accept", "*/*");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
        return headers;
    }

    public static Map<String, String> getEastBKWebHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "qgqp_b_id=3159c429a9f9980500703d5341bea318; st_si=62769308102406; st_asi=delete; HAList=ty-0-002643-%u4E07%u6DA6%u80A1%u4EFD%2Cty-90-BK1039-%u7535%u5B50%u5316%u5B66%u54C1%2Cty-90-BK0145-%u4E0A%u6D77%u677F%u5757%2Cty-90-BK0896-%u767D%u9152%2Cty-0-300398-%u98DE%u51EF%u6750%u6599; p_origin=https%3A%2F%2Fpassport2.eastmoney.com; mtp=1; ct=2_jL3QZBMIX6XzDQmk3alBF0hN51hZpf22WpVYFBCzobOP2Xm-VVhkIIuUq7FJZgw8EYvFT7zkZZh4vagfokzSeAh6SX1-EHgIO5TTmFzAKXUW8-AbzINyu2iiQHKL13gDRZ6DXpJfUrOODcpfUtCVuFbL3Xlyp-c0AkXz-KZvI; ut=FobyicMgeV5kZz00Ot3UbweF9-4Uhjwa6xNkABS5WUWGRd5dlSR49ZDFPsYieq4r1sTmIWpizEGBLtINqyn_YcCAw348Nw2n99IEUdQfqVENbz2bthjsDMmjn94cxN8xaZOH-yZ1wxmJe1ljijpNfdqcHuDF0cSkwxCkO3qCNRJpK79XzuW7LuKjtzBwCuw9WCpJEC-WTMax_4Ga6QgT_veWxXUxY-1uPL3r8K7efHwis8E0X0tl36GdNNatyN9OdqWy_MwYSyf57hr5wpfDXg; pi=3914356125019460%3Bu3914356125019460%3B%E8%82%A1%E5%8F%8B189d763T18%3BgjSIQrPC5LukdtUgiWaWop85NsKcpw8voKeP9LcA7xnj4Gs5Dam8admVCQu5W%2B3YqVgtGeTzG%2BwftUIlrfoh1T95Wovkm%2F5%2BZnR08FziZmMQpGYN8pCaDZ53%2F9LpQyfWkW0C8ddn7%2Fl6KM3g7STdOtrwxyDysJXnRA52u1g4mMhgxf62iFra5abP1gQOnAVqnbf4Tn%2Bj%3BEDoCSbjaGaz0H%2B%2BEaY3HuX7zhhUDJ3qajlBuAV6rDegm3RpD%2F%2F8Q%2Fl5nPyOXQFQblmKTJxqi58KbVOBtk4ocTllWUz5L%2FEEAAzvn626F8CYuZ%2Fiz4dMEbPKTEmbU%2FD39jnX%2BHUUkFij9AY27a%2BgZJ8xa6L09pQ%3D%3D; uidal=3914356125019460%e8%82%a1%e5%8f%8b189d763T18; sid=159759623; vtpst=|; st_pvi=68340009970843; st_sp=2023-06-05%2015%3A15%3A46; st_inirUrl=https%3A%2F%2Fwww.google.com%2F; st_sn=19; st_psi=20230828195248734-113200313002-0813850113");
        headers.put("Host", "5.push2.eastmoney.com");
        headers.put("Referer", "http://quote.eastmoney.com/center/boardlist.html");
        //headers.put("Content-Type", "application/json");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Accept", "*/*");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
        return headers;
    }

    public static Map<String, String> getEastBKDetailWebHeaders(String bkcode) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "qgqp_b_id=3159c429a9f9980500703d5341bea318; mtp=1; ct=2_jL3QZBMIX6XzDQmk3alBF0hN51hZpf22WpVYFBCzobOP2Xm-VVhkIIuUq7FJZgw8EYvFT7zkZZh4vagfokzSeAh6SX1-EHgIO5TTmFzAKXUW8-AbzINyu2iiQHKL13gDRZ6DXpJfUrOODcpfUtCVuFbL3Xlyp-c0AkXz-KZvI; ut=FobyicMgeV5kZz00Ot3UbweF9-4Uhjwa6xNkABS5WUWGRd5dlSR49ZDFPsYieq4r1sTmIWpizEGBLtINqyn_YcCAw348Nw2n99IEUdQfqVENbz2bthjsDMmjn94cxN8xaZOH-yZ1wxmJe1ljijpNfdqcHuDF0cSkwxCkO3qCNRJpK79XzuW7LuKjtzBwCuw9WCpJEC-WTMax_4Ga6QgT_veWxXUxY-1uPL3r8K7efHwis8E0X0tl36GdNNatyN9OdqWy_MwYSyf57hr5wpfDXg; pi=3914356125019460%3Bu3914356125019460%3B%E8%82%A1%E5%8F%8B189d763T18%3BgjSIQrPC5LukdtUgiWaWop85NsKcpw8voKeP9LcA7xnj4Gs5Dam8admVCQu5W%2B3YqVgtGeTzG%2BwftUIlrfoh1T95Wovkm%2F5%2BZnR08FziZmMQpGYN8pCaDZ53%2F9LpQyfWkW0C8ddn7%2Fl6KM3g7STdOtrwxyDysJXnRA52u1g4mMhgxf62iFra5abP1gQOnAVqnbf4Tn%2Bj%3BEDoCSbjaGaz0H%2B%2BEaY3HuX7zhhUDJ3qajlBuAV6rDegm3RpD%2F%2F8Q%2Fl5nPyOXQFQblmKTJxqi58KbVOBtk4ocTllWUz5L%2FEEAAzvn626F8CYuZ%2Fiz4dMEbPKTEmbU%2FD39jnX%2BHUUkFij9AY27a%2BgZJ8xa6L09pQ%3D%3D; uidal=3914356125019460%e8%82%a1%e5%8f%8b189d763T18; sid=159759623; vtpst=|; st_si=42038180615794; st_asi=delete; HAList=ty-90-BK1015-%u80FD%u6E90%u91D1%u5C5E%2Cty-90-BK0485-%u65C5%u6E38%u9152%u5E97%2Cty-90-BK1039-%u7535%u5B50%u5316%u5B66%u54C1%2Cty-90-BK0732-%u8D35%u91D1%u5C5E%2Cty-90-BK0475-%u94F6%u884C%2Cty-0-002643-%u4E07%u6DA6%u80A1%u4EFD%2Cty-90-BK0145-%u4E0A%u6D77%u677F%u5757%2Cty-90-BK0896-%u767D%u9152%2Cty-0-300398-%u98DE%u51EF%u6750%u6599; st_pvi=68340009970843; st_sp=2023-06-05%2015%3A15%3A46; st_inirUrl=https%3A%2F%2Fwww.google.com%2F; st_sn=13; st_psi=20231206151135881-113300300992-7647546083");
        headers.put("Host", "push2.eastmoney.com");
        headers.put("Referer", "https://data.eastmoney.com/bkzj/"+bkcode+".html");
        //headers.put("Content-Type", "application/json");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Accept", "*/*");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
        return headers;
    }


    public static Map<String, String> getEastConceptWebHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "qgqp_b_id=3159c429a9f9980500703d5341bea318; mtp=1; ct=2_jL3QZBMIX6XzDQmk3alBF0hN51hZpf22WpVYFBCzobOP2Xm-VVhkIIuUq7FJZgw8EYvFT7zkZZh4vagfokzSeAh6SX1-EHgIO5TTmFzAKXUW8-AbzINyu2iiQHKL13gDRZ6DXpJfUrOODcpfUtCVuFbL3Xlyp-c0AkXz-KZvI; ut=FobyicMgeV5kZz00Ot3UbweF9-4Uhjwa6xNkABS5WUWGRd5dlSR49ZDFPsYieq4r1sTmIWpizEGBLtINqyn_YcCAw348Nw2n99IEUdQfqVENbz2bthjsDMmjn94cxN8xaZOH-yZ1wxmJe1ljijpNfdqcHuDF0cSkwxCkO3qCNRJpK79XzuW7LuKjtzBwCuw9WCpJEC-WTMax_4Ga6QgT_veWxXUxY-1uPL3r8K7efHwis8E0X0tl36GdNNatyN9OdqWy_MwYSyf57hr5wpfDXg; pi=3914356125019460%3Bu3914356125019460%3B%E8%82%A1%E5%8F%8B189d763T18%3BgjSIQrPC5LukdtUgiWaWop85NsKcpw8voKeP9LcA7xnj4Gs5Dam8admVCQu5W%2B3YqVgtGeTzG%2BwftUIlrfoh1T95Wovkm%2F5%2BZnR08FziZmMQpGYN8pCaDZ53%2F9LpQyfWkW0C8ddn7%2Fl6KM3g7STdOtrwxyDysJXnRA52u1g4mMhgxf62iFra5abP1gQOnAVqnbf4Tn%2Bj%3BEDoCSbjaGaz0H%2B%2BEaY3HuX7zhhUDJ3qajlBuAV6rDegm3RpD%2F%2F8Q%2Fl5nPyOXQFQblmKTJxqi58KbVOBtk4ocTllWUz5L%2FEEAAzvn626F8CYuZ%2Fiz4dMEbPKTEmbU%2FD39jnX%2BHUUkFij9AY27a%2BgZJ8xa6L09pQ%3D%3D; uidal=3914356125019460%e8%82%a1%e5%8f%8b189d763T18; sid=159759623; vtpst=|; st_si=42038180615794; st_asi=delete; HAList=ty-90-BK1134-%u7B97%u529B%u6982%u5FF5%2Cty-90-BK1153-%u591A%u6A21%u6001AI%2Cty-90-BK1015-%u80FD%u6E90%u91D1%u5C5E%2Cty-90-BK0485-%u65C5%u6E38%u9152%u5E97%2Cty-90-BK1039-%u7535%u5B50%u5316%u5B66%u54C1%2Cty-90-BK0732-%u8D35%u91D1%u5C5E%2Cty-90-BK0475-%u94F6%u884C%2Cty-0-002643-%u4E07%u6DA6%u80A1%u4EFD%2Cty-90-BK0145-%u4E0A%u6D77%u677F%u5757%2Cty-90-BK0896-%u767D%u9152; st_pvi=68340009970843; st_sp=2023-06-05%2015%3A15%3A46; st_inirUrl=https%3A%2F%2Fwww.google.com%2F; st_sn=50; st_psi=20231220092537398-113200313002-7000064764");
        headers.put("Host", "91.push2.eastmoney.com");
        headers.put("Referer", "http://quote.eastmoney.com/center/boardlist.htm");
        //headers.put("Content-Type", "application/json");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Accept", "*/*");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
        return headers;
    }
}
