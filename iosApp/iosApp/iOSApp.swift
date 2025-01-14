import SwiftUI
import GoogleMaps

@main
struct iOSApp: App {
    init(){
        GMSServices.provideAPIKey("AIzaSyAC41hpYKVEJTs761hCq7szt3kqqcUCHlA")
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
