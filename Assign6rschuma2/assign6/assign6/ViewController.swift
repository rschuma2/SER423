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
    
    var selectedPlace:String=""
    var places:[String]=[String]()

    var urlString:String = "http://127.0.0.1:8080"
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.urlString = self.setURL()
        self.callGetNamesNUpdateStudentsPicker()
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

    func callGetNamesNUpdateStudentsPicker() {
        let aConnect:PlaceLibraryStub = PlaceLibraryStub(urlString: urlString)
        let _:Bool = aConnect.getNames(callback: { (res: String, err: String?) -> Void in
            if err != nil {
                NSLog(err!)
            }else{
                NSLog(res)
                if let data: Data = res.data(using: String.Encoding.utf8){
                    do{
                        let dict = try JSONSerialization.jsonObject(with: data,options:.mutableContainers) as?[String:AnyObject]
                        self.places = (dict!["result"] as? [String])!
                        self.places = Array(self.places).sorted()
                        //self.studSelectTF.text = ((self.students.count>0) ? self.students[0] : "")
                        //self.studentPicker.reloadAllComponents()
                        if self.places.count > 0 {
                            self.callGetNPopulatUIFields(self.places[0])
                        }
                    } catch {
                        print("unable to convert to dictionary")
                    }
                }
                
            }
        })  // end of method call to getNames
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
                        //self.studentNumTF.text = "\(aStud.studentid)"
                        self.placeNameLabel.text = aPlace.name
                        //self.takes = Array(aStud.takes).sorted()
                        //self.takesTF.text = ((self.takes.count > 0) ? self.takes[0] : "")
                        //self.takesPicker.reloadAllComponents()
                    } catch {
                        NSLog("unable to convert to dictionary")
                    }
                }
            }
    })
    }

}

