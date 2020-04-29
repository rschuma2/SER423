//
//  PlaceTableViewController.swift
//  Lab4
//
//  Created by ry of rykel on 4/12/20.
//  Copyright © 2020 Ryan Schumacher. All rights reserved.
//

import UIKit

class PlaceTableViewController: UITableViewController {

    var places:[String:Place] = [String:Place]()
    var names:[String]=[String]()
    var pets:[(name:String,type:String)] = [("Tim", "a teacher"),("Gary", "a fool")]
    
    var urlString:String = "http://127.0.0.1:8080"
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.urlString = self.setURL()
        self.callGetNamesNUpdateStudentsPicker()
        
        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem

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
                        //self.studSelectTF.text = ((self.students.count>0) ? self.students[0] : "")
                        //self.studentPicker.reloadAllComponents()
                        if self.names.count > 0 {
                            self.callGetNPopulatUIFields(self.names[0])
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
                        //self.placeNameLabel.text = aPlace.name
                        //self.takes = Array(aStud.takes).sorted()
                        //self.takesTF.text = ((self.takes.count > 0) ? self.takes[0] : "")
                        //self.takesPicker.reloadAllComponents()
                        self.tableView.reloadData()
                    } catch {
                        NSLog("unable to convert to dictionary")
                    }
                }
            }
    })
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
        // Configure the cell...
        /*let aPet:(name:String,type:String) = pets[indexPath.row]
        cell.textLabel?.text = aPet.name
        cell.detailTextLabel?.text = aPet.type
        return cell
 *//*
        let aPlace = places[names[indexPath.row]]! as Place
        cell.textLabel?.text = aPlace.name
        cell.detailTextLabel?.text = aPlace.description
        return cell
 */
        cell.textLabel?.text=names[indexPath.row]
        return cell
    }
    

    /*
    // Override to support conditional editing of the table view.
    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    */

    /*
    // Override to support editing the table view.
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            // Delete the row from the data source
            tableView.deleteRows(at: [indexPath], with: .fade)
        } else if editingStyle == .insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }
    }
    */

    /*
    // Override to support rearranging the table view.
    override func tableView(_ tableView: UITableView, moveRowAt fromIndexPath: IndexPath, to: IndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(_ tableView: UITableView, canMoveRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */

    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
        //NSlog("segue identifier is \(segue.identifier)")
        if segue.identifier=="PlaceDescription" {
            let viewController:ViewController=segue.destination as! ViewController
            let indexPath=self.tableView.indexPathForSelectedRow!
            viewController.names=self.names
            viewController.selectedPlace=self.names[indexPath.row]
        }
    }
    

}
