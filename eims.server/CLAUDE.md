# CLAUDE.md

本檔案提供 Claude Code (claude.ai/code) 在此儲存庫中工作時的指引。

## 最高指導原則

**所有與使用者的互動、回覆、以及程式碼／文件中的註解，一律使用繁體中文。** 程式識別字、API 名稱、錯誤訊息字串、設定鍵值維持英文原樣，但其周圍說明文字必須為繁體中文。

## 建置與執行

Gradle wrapper（Java 8，`sourceCompatibility = 1.8`）。相依套件為 `lib/` 底下的扁平 JAR（不走 Maven Central）— `compile fileTree(dir: 'lib', include: '*.jar')`。

```bash
./gradlew build          # 產生 build/libs/ROOT.war（war plugin，baseName=eims.server，archiveName=ROOT.war）
./gradlew war            # 只打 WAR
./gradlew processResources  # 會連帶執行 copyDaoXml + copyDaoXml2（將 MyBatis XML mapper 複製到 build/classes）
```

`build.gradle` 中無 test task、無 lint 設定。JavaCompile 使用 `-Xlint:unchecked -Xlint:deprecation` 與 UTF-8 編碼。

兩種執行模式都已配線，但 application-runner 模式被註解掉：

- WAR 部署（目前預設）：`apply plugin: 'war'`，進入點為 `ApplicationMain extends SpringBootServletInitializer`（亦有 `WarInitializerApplication`）。預期由容器提供 Tomcat — 參見 `providedRuntime fileTree(dir: 'lib', include: 'spring-boot-starter-tomcat-*.jar')`。
- 獨立執行：在 `build.gradle` 中解除註解 `apply plugin: 'application'` 與 `mainClassName = 'eims.ApplicationMain'`。

應用程式監聽埠號 `9100`，context path 為 `/eims`（見 `src/main/resources/application.properties`）。

## 伺服器 vs 本機切換

`eims.web.constants.BxConstants.Default.IS_SERVER` 是一個**編譯期**布林值，同時切換 `EimsDatabaseConfig` 與 `MetaDatabaseConfig` 的行為：

- `IS_SERVER = true` → 兩個資料來源都透過 JNDI `java:/EIMSNXA` 解析（WAS 部署，例如 Tomcat/Jeus）。
- `IS_SERVER = false` → 使用 `DataSourceBuilder.create().build()`，從 `spring.eims.datasource.*` / `spring.meta.datasource.*` 屬性建構（本機 Tomcat 開發）。需在 `application.properties` 中解除對應 `url/username/password/driver-class-name` 區塊的註解。

切換方式為直接修改 `BxConstants.java` — 沒有 profile 或環境變數開關。

## 架構

Spring Boot 1.4.x WAR 應用程式，封裝為 `ROOT.war`，從 `src/main/resources/static/` 提供 AngularJS SPA。

### 後端結構（`src/main/java/eims/`）

- `ApplicationMain` / `WarInitializerApplication` — Spring Boot 進入點。兩者都標註 `@SpringBootApplication @EnableTransactionManagement @EnableAsync @EnableScheduling`。`ApplicationMain` 另外加上 `@ServletComponentScan` 以及一個 5-thread 的 `ScheduledExecutorFactoryBean`。
- `config/` — `CommonConfiguration`（MessageSource、自訂 Jackson `ObjectMapper`，透過 `CustomObjectMapper`/`NullSerializer`；於 `/**` 註冊 `RequestInterceptor`，明確排除 `/login`、`/logout`、`/message`、`/codes`、`/menus`、`/bxmlogin`、`/intrfccoms/deploy/terminal`、`/trxs`、`/trxs/**`、`/userstest/test`）、`SSOFilterConfig`（CAS）、`HomeController`。
- `web/EimsDatabaseConfig` + `web/MetaDatabaseConfig` — **兩組 MyBatis SqlSessionFactory**，分別搭配獨立的 `@MapperScan`：
  - `eims.web.dao` mapper → `eimsSqlSessionFactory`（primary，提供 `transactionManager` bean）→ XML 位於 `classpath:eims/web/dao/xml/*.xml`。
  - `eims.web.meta.dao` mapper → `metaSqlSessionFactory` → XML 位於 `classpath:eims/web/meta/dao/xml/*.xml`。
  - MyBatis XML 檔與 Java mapper 一同放在 `src/main/java/.../xml/` 之下，並**由 Gradle 的 `copyDaoXml` / `copyDaoXml2` task 複製進建置產出物**（掛在 `processResources` 上）。新增的 mapper XML 必須放在那兩個目錄中。
- `web/controller/` — REST controller，依領域拆分（Menu、Role、Perm、User、App、Biz、Enc、Mask、Trx、Intrfccom、Msglayout、Mappingfunc、Srsys、Depolysys、Extrnlinst、Commcode、Meta、Actionhist、Main）。命名慣例：`XxxController` ↔ `XxxService` ↔ `XxxDao`（+ `XxxDao.xml`）。
- `web/dao/`、`web/service/`、`web/dto/`（含 `dto/ui`、`dto/table`）、`web/constants/`、`web/excel/`、`web/exception/`、`web/interceptor/`、`web/utils/` — 標準的分層拆分。
- `web/meta/dao/` — 次要資料來源（目前只有 `BcMetaDao`）。

### 前端（`src/main/resources/static/app/`）

AngularJS SPA。`app.js` / `app.config.js` / `app.route.config.js` 連同 `.map` 一起進版控 — 視為來源真相，本儲存庫內沒有獨立的前端建置流程。Views 依畫面 ID 組織（`views/SCR0001/SCR0001.controller.js` + `SCR0001.html`），另有 `views/popup/` 與 `views/wrap/`。共用元件放在 `common/directive/` 與 `common/service/`。資產（w2ui、jQuery、jQuery-UI、ui-bootstrap、pdf、bxjs、bxdi 字型、exo2/noto）位於 `static/assets/`。

### 外部整合

`lib/` 中的廠商 JAR 限制了執行環境：`cas-client-core` / `cas-client-support-saml`（透過 `SSOFilterConfig` 進行 SSO）、`INICrypto`、`fasoo-jni`、`fcwpkg_jni`、`external_v4.1.3.06_BCcard_01`（第三方 JNI／加密／信用卡整合 — 隱含需要 Linux 系列的原生函式庫）。

## 需保留的慣例

- 新增 DB 實體 = 在同一個 `xml/` 子目錄下加入 `XxxDao.java`（介面，MyBatis mapper）與 `XxxDao.xml`，再加上 `XxxService`、`XxxController`。由 package 決定使用哪個資料來源（`eims.web.dao` vs `eims.web.meta.dao`）— `@MapperScan` 會自動路由。
- 需要繞過 `RequestInterceptor` 的端點（auth/menu/login 等）必須加進 `CommonConfiguration.webMvcConfigurerAdapter()` 中的 `excludePathPatterns(...)` 清單。
- JSON 序列化走 `CustomObjectMapper`（null 處理透過 `CustomNullStringSerializerProvider` / `NullSerializer`），並設定 `FAIL_ON_UNKNOWN_PROPERTIES = true` — 傳入的 DTO 必須宣告 client 傳來的每一個欄位。
- 編碼端到端皆為 UTF-8（Gradle 編譯與 Spring）；保留 LF 行尾（近期 commit `d4795586` 已強制 LF）。
- **新增 / 修改 controller endpoint 時，必須同步更新 `docs/api/`**：在 `docs/api/openapi.yaml` 之 `paths` 區段加 / 改對應條目，並更新對應之 `docs/api/<domain>.md` 表格。檔案上傳 endpoint 使用 `multipart/form-data`、二進位下載使用 `application/octet-stream`、排除於 `RequestInterceptor` 之 endpoint 標 `x-interceptor-excluded: true`。完成後跑 `./scripts/check-api-catalog.sh` 驗證 controller 與 openapi.yaml 之 (method, path) 集合一致。
