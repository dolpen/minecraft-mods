## 前提

### JDK

* 9+ は無理のよう JDK 8 の新しいビルドから選ぶ
  * MCP / FMLあたりのクラスリパッケージでエラー吐いて環境構築に失敗する
  * [OpenJDK](https://developers.redhat.com/products/openjdk/download) か [Oracle](https://www.oracle.com/technetwork/java/javase/downloads/index.html) の好きな方で
  * 変にググって ri-8u40 とかの変なビルド入れると後悔します
  * 古いと `DST Root CA X3` も `ISRG Root X1` などの SSL ルート証明書がなく `files.minecraftforge.net` などが解決できなくてビルドでいろいろ詰みます。

## ワークスペースの構築 

ForgeのグルーコードとかMCPとか諸々入れて依存性解決する必要があります。

### IDEA

* fetch する
* Gradle プロジェクトとしてインポート
  * `build.gradle` を選択してロード
  * プロジェクトとして取り込むかモジュールとするかは任意
* `Gradle` ビューを開く
  * `Tasks > forgegradle > setupDecompWorkspace` を実行
  
### CLI

```
./gradlew setupDecompWorkspace
```

## ビルドとデプロイ

`build` タスクを実行する

### CI

[GitHub Actions](https://help.github.com/ja/github/automating-your-workflow-with-github-actions) を使っています

* Release を作成すると、ビルドされた成果物がアタッチされます。
 [JasonEtco/upload-to-release](https://github.com/JasonEtco/upload-to-release) を使っているので該当ワークフローを `release` 以外のトリガーで発火しないようにしてください


## tips

* `ForgeGradle` とは？
  * mod 開発環境を簡単に整理するための Gradle プラグイン
  * Gradle 5+ には非対応のため 4 系を使う
* `setupDecompWorkspace` とは？
  * `ForgeGradle` 内で定義されたワークスペース構築タスク
  * Minecraft 本体 + MCP や ForgeModLoader のパッチをDLする
  * DL したもののクラスの展開、リパッケージから依存解決など色々やってる
* 環境変数 `JAVA_HOME` に JDK 8 を設定したくないのですが
  * CLI なら事前に export しとくなりコマンドプロンプトで set してください（一時設定になります）
  * IDEA は 使用する JDK を Gradle プラグインオプションで設定すれば大体良し
    * Run/Debug Configuration でテストクライアント起動も制御可能
