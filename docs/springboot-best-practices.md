# SpringBoot コーディング規約 (レビュー基準)

## C-01: コントローラ (Controller)
- **Thin Controller**: アクションはリクエスト受付、バリデーション、レスポンス返却に集中。ビジネスロジックはServiceに委譲。
- **@RequestBody + @Valid**: リクエストボディのバリデーションには `@Valid` を活用。
- **ResponseEntity**: レスポンスには適切なHTTPステータスコードを返すこと。

## S-01: サービス (Service)
- **ビジネスロジック集約**: ビジネスロジックはService層に集約し、Controllerから分離。
- **コンストラクタインジェクション**: `@Autowired` フィールドインジェクションではなく、`private final` + コンストラクタインジェクションを使用。
- **トランザクション管理**: 複数のDB操作を伴う処理には `@Transactional` を適用。

## T-01: テスト (JUnit 5)
- テストは実装ではなく**振る舞い**をテストすること。
- **@Nested + @DisplayName** でテストを構造化し、可読性を確保。
- テスト名は日本語の `@DisplayName` で期待される振る舞いを明記。
