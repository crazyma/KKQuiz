這是一個簡單的猜歌遊戲，主要是使用 KKBOX SDK 進行開發

## 下載
<a href='https://play.google.com/store/apps/details?id=com.beibeilab.kkquiz&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Google Play立即下載' src='https://play.google.com/intl/en_us/badges/images/generic/zh-tw_badge_web_generic.png'/></a>

## Develop
開發詳情可直接參照 [KKBox for developer](https://developer.kkbox.com/#/)<br/>
基本上沒有遇到太多問題，只有一些小細節:


1. 叫記得 在 build.gradle 加上: `implementation 'com.koushikdutta.ion:ion:2.2.1'`
2. 在呼叫 `api.searchFetcher`要先設定 **q**: `api.searchFetcher.setSearchCriteria(q="Happy")`
