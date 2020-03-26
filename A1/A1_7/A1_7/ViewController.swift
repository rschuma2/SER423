//
//  ViewController.swift
//  A1_7
//
//  Created by ry of rykel on 3/21/20.
//  Copyright © 2020 Ryan Schumacher. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }

    @IBOutlet weak var nameTF: UITextField!

    @IBOutlet weak var descriptionTF: UITextField!

    @IBOutlet weak var categoryTF: UITextField!

    @IBOutlet weak var addresstitleTF: UITextField!

    @IBOutlet weak var address_streetTF: UITextField!
    
    @IBOutlet weak var elevationTF: UITextField!

    @IBOutlet weak var latitudeTF: UITextField!

    @IBOutlet weak var longitudeTF: UITextField!

    @IBAction func showPlaceButtonClicked(_ sender: Any) {
        nameTF.text = p.name
        descriptionTF.text = p.description
        categoryTF.text = p.category
        addresstitleTF.text = p.addressTitle
        address_streetTF.text = p.addressStreet[0]
        elevationTF.text = "\(p.elevation)" as String
        latitudeTF.text = "\(p.latitude)" as String
        longitudeTF.text = "\(p.longitude)" as String
        
    }
}

