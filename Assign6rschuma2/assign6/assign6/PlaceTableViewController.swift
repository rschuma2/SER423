//
//  PlaceTableViewController.swift
//  Lab4
//
//  Created by ry of rykel on 4/12/20.
//  Copyright © 2020 Ryan Schumacher. All rights reserved.
//

import UIKit

class PlaceTableViewController: UITableViewController {

    var names:[String]=[String]()
    
    var urlString:String = "http://127.0.0.1:8080"
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.urlString = self.setURL()
        self.callGetNamesNUpdateStudentsPicker()
        
        // add an edit button, which is handled by the table view editing forRowAt
        self.navigationItem.leftBarButtonItem = self.editButtonItem
        // place an add button on the right side of the nav bar for adding a student
        // call addStudent function when clicked.
        let addButton = UIBarButtonItem(barButtonSystemItem: UIBarButtonItem.SystemItem.add, target: self, action: #selector(PlaceTableViewController.addPlace))
        self.navigationItem.rightBarButtonItem = addButton
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
                        self.names = (dict!["result"] as? [String])!
                        self.names = Array(self.names).sorted()
                        if self.names.count > 0 {
                            self.tableView.reloadData()
                        }
                    } catch {
                        print("unable to convert to dictionary")
                    }
                }
                
            }
        })  // end of method call to getNames
    }
    
    // Support editing of the table view. Note, edit button must have been added
    // to the navigationitem (in this case left side) explicitly (view did load)
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        print("tableView editing row at: \(indexPath.row)")
        if editingStyle == .delete {
            let selectedPlace:String = names[indexPath.row]
            print("deleting the student \(selectedPlace)")
            let aConnect:PlaceLibraryStub = PlaceLibraryStub(urlString: urlString)
            let _:Bool = aConnect.remove(placeName: names[indexPath.row], callback: { _,_  in
                self.callGetNamesNUpdateStudentsPicker()
                })
            //tableView.deleteRows(at: [indexPath], with: .fade)
            // don't need to reload data, using delete to make update
        }
    }
    
    @objc func addPlace(_ sender: Any) {
        let promptND = UIAlertController(title: "New Place Name", message: "Enter all Fields for Place", preferredStyle: UIAlertController.Style.alert)
        // if the user cancels, we don't want to add an annotation or pin
        promptND.addAction(UIAlertAction(title: "Cancel", style: UIAlertAction.Style.default, handler: nil))
        // setup the OK action and the closure to be executed when/if OK selected
        promptND.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: { (action) -> Void in
            //print("you entered title: \(promptND.textFields?[0].text)")
            let newPlaceName = (promptND.textFields?[0].text)!
            let newPlaceDescription = (promptND.textFields?[1].text)!
            let newPlaceCategory = (promptND.textFields?[2].text)!
            let newPlaceAddressTitle = (promptND.textFields?[3].text)!
            let newPlaceAddressStreet = (promptND.textFields?[4].text)!
            let newPlaceLatitude = Double((promptND.textFields?[5].text)!)
            let newPlaceLongitude = Double((promptND.textFields?[6].text)!)
            let newPlaceElevation = Double((promptND.textFields?[7].text)!)
            let newPlaceImage = (promptND.textFields?[8].text)!
            if !self.names.contains(newPlaceName) {
                let aPlace:Place = Place(dict:["name":newPlaceName, "description":newPlaceDescription, "category":newPlaceCategory, "address-title":newPlaceAddressTitle, "address-street":newPlaceAddressStreet, "latitude":newPlaceLatitude!, "longitude":newPlaceLongitude!, "elevation":newPlaceElevation!, "image":newPlaceImage])
                let aConnect:PlaceLibraryStub = PlaceLibraryStub(urlString: self.urlString)
                let _:Bool = aConnect.add(place: aPlace,callback: { _,_  in
                    print("\(aPlace.name) added as: \(aPlace.toJsonString())")
                    self.callGetNamesNUpdateStudentsPicker()})
            }
        }))
        promptND.addTextField(configurationHandler: {(textField: UITextField!) in textField.placeholder = "name"})
        promptND.addTextField(configurationHandler: {(textField: UITextField!) in textField.placeholder = "description"})
        promptND.addTextField(configurationHandler: {(textField: UITextField!) in textField.placeholder = "category"})
        promptND.addTextField(configurationHandler: {(textField: UITextField!) in textField.placeholder = "address title"})
        promptND.addTextField(configurationHandler: {(textField: UITextField!) in textField.placeholder = "address street"})
        promptND.addTextField(configurationHandler: {(textField: UITextField!) in textField.placeholder = "latitude"})
        promptND.addTextField(configurationHandler: {(textField: UITextField!) in textField.placeholder = "longitude"})
        promptND.addTextField(configurationHandler: {(textField: UITextField!) in textField.placeholder = "!elevation"})
        promptND.addTextField(configurationHandler: {(textField: UITextField!) in textField.placeholder = "image"})
        present(promptND, animated: true, completion: nil)
    }


      
    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return names.count
    }

    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "PlaceCell", for: indexPath)
        cell.textLabel?.text=names[indexPath.row]
        return cell
    }

    
    // MARK: - Navigation

    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
        if segue.identifier=="PlaceDescription" {
            let viewController:ViewController=segue.destination as! ViewController
            let indexPath=self.tableView.indexPathForSelectedRow!
            viewController.names=self.names
            viewController.selectedPlace=self.names[indexPath.row]
        }
    }
    

}
