/*
* Copyright 2020 Ryan Schumacher,
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
* ASU and SER423 instructor and staff have the right to build and evaluate this
* software package for the purpose of determining a grade and program assessment
*
* ViewController.swift
* assign6
* Purpose: Description view for a Place displayed based on table row selected in
* PlaceTableViewController.
*
* Ser423 Mobile Applications
* see http://pooh.poly.asu.edu/Mobile
* @author Ryan Schumacher mailto:rschuma2@asu.edu
*         Software Engineering, BSSE, IAFSE, Online
* @version April, 29, 2020
*/
import UIKit

class ViewController: UIViewController {

    @IBOutlet weak var placeNameLabel: UILabel!
    @IBOutlet weak var nameTF: UITextField!
    @IBOutlet weak var placeDescriptionLabel: UILabel!
    @IBOutlet weak var descTF: UITextView!
    @IBOutlet weak var placeCategoryLabel: UILabel!
    @IBOutlet weak var catTF: UITextField!
    @IBOutlet weak var placeAddressTitleLabel: UILabel!
    @IBOutlet weak var titleTF: UITextField!
    @IBOutlet weak var placeAddressStreetLabel: UILabel!
    @IBOutlet weak var streetTF: UITextField!
    @IBOutlet weak var placeLatitudeLabel: UILabel!
    @IBOutlet weak var latTF: UITextField!
    @IBOutlet weak var placeLongitudeLabel: UILabel!
    @IBOutlet weak var lonTF: UITextField!
    @IBOutlet weak var placeElevationLabel: UILabel!
    @IBOutlet weak var elevTF: UITextField!
    @IBOutlet weak var placeImageLabel: UILabel!
    @IBOutlet weak var imgTF: UITextField!
    
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
                        self.nameTF.text = aPlace.name
                        self.descTF.text = aPlace.description
                        self.catTF.text = aPlace.category
                        self.titleTF.text = aPlace.address_title
                        self.streetTF.text = aPlace.address_street
                        self.latTF.text = "\(aPlace.latitude)"
                        self.lonTF.text = "\(aPlace.longitude)"
                        self.elevTF.text = "\(aPlace.elevation)"
                        self.imgTF.text = aPlace.image
                    } catch {
                        NSLog("unable to convert to dictionary")
                    }
                }
            }
    })
    }

}

