// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "SmsPlugin",
    platforms: [.iOS(.v14)],
    products: [
        .library(
            name: "SmsPlugin",
            targets: ["SmsPluginPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "7.0.0")
    ],
    targets: [
        .target(
            name: "SmsPluginPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/SmsPluginPlugin"),
        .testTarget(
            name: "SmsPluginPluginTests",
            dependencies: ["SmsPluginPlugin"],
            path: "ios/Tests/SmsPluginPluginTests")
    ]
)