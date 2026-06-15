# Main API

`MainController` — 登入、登出、SSO、使用者資訊、語系切換。

> Base: `http://<host>:9100/eims`
> 排除於 `RequestInterceptor` 之 endpoint 於下表標 ⛔。

| Method | Path | 說明 | Interceptor |
|---|---|---|---|
| POST | `/login` | 一般帳密登入 | ⛔ excluded |
| GET | `/eims/sso` | CAS SSO 入口 | applied |
| GET | `/changelang` | 切語系（query `lang`） | applied |
| GET | `/logininfosso` | SSO 登入後使用者資訊 | applied |
| GET | `/bxmlogin` | BXM 登入入口 | ⛔ excluded |
| GET | `/userInfo` | 目前登入使用者資訊 | applied |
| POST | `/logout` | 登出 | ⛔ excluded |

詳見 [`openapi.yaml`](./openapi.yaml) tag `Main`。
