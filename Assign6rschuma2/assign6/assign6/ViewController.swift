//
//  ViewController.swift
//  assign6
//
//  Created by ry of rykel on 4/27/20.
//  Copyright © 2020 Ryan Schumacher. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    @IBOutlet weak var placeNameLabel: UILabel!
    @IBOutlet weak var placeDescriptionLabel: UILabel!
    @IBOutlet weak var placeCategoryLabel: UILabel!
    @IBOutlet weak var placeAddressTitleLabel: UILabel!
    @IBOutlet weak var placeAddressStreetLabel: UILabel!
    @IBOutlet weak var placeLatitudeLabel: UILabel!
    @IBOutlet weak var placeLongitudeLabel: UILabel!
    @IBOutlet weak var placeElevationLabel: UILabel!
    @IBOutlet weak var placeImageLabel: UILabel!
    
    var selectedPlace:String = "unknown"
    var names:[String]=[String]()

    var urlString:String = "http://127.0.0.1:8080"
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.urlString = self.setURL()
        self.callGetNPopulatUIFields(self.selectedPlace)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func setURL () -> String {
        var serverhost:String = "localhost"
        var jsonrpcport:String = "8080"
        var serverprotocol:String = "http"
        // access and log all of the app settings from the settings bundle resource
        if let path = Bundle.main.path(forResource: "ServerInfo", ofType: "plist"){
            // defaults
            if let dict = NSDictionary(contentsOfFile: path) as? [String:AnyObject] {
                serverhost = (dict["server_host"] as? String)!
                jsonrpcport = (dict["jsonrpc_port"] as? String)!
                serverprotocol = (dict["server_protocol"] as? String)!
            }
        }
        print("setURL returning: \(serverprotocol)://\(serverhost):\(jsonrpcport)")
        return "\(serverprotocol)://\(serverhost):\(jsonrpcport)"
    }
    
    func callGetNPopulatUIFields(_ name: String){
        let aConnect:PlaceLibraryStub = PlaceLibraryStub(urlString: urlString)
        let _:Bool = aConnect.get(name: name, callback: { (res: String, err: String?) -> Void in
            if err != nil {
                NSLog(err!)
            }else{
                NSLog(res)
                if let data: Data = res.data(using: String.Encoding.utf8){
                    do{
                        let dict = try JSONSerialization.jsonObject(with: data,options:.mutableContainers) as?[String:AnyObject]
                        let aDict:[String:AnyObject] = (dict!["result"] as? [String:AnyObject])!
                        let aPlace:Place = Place(dict: aDict)
                        self.placeNameLabel.text = aPlace.name
                        self.placeDescriptionLabel.text = aPlace.description
                        self.placeCategoryLabel.text = aPlace.category
                        self.placeAddressTitleLabel.text = aPlace.address_title
                        self.placeAddressStreetLabel.text = aPlace.address_street
                        self.placeLatitudeLabel.text = "\(aPlace.latitude)"
                        self.placeLongitudeLabel.text = "\(aPlace.longitude)"
                        self.placeElevationLabel.text = "\(aPlace.elevation)"
                        self.placeImageLabel.text = aPlace.image
                    } catch {
                        NSLog("unable to convert to dictionary")
                    }
                }
            }
    })
    }

}

