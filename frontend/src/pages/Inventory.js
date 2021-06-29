import React, { Component } from "react";
import PropTypes from "prop-types";
import styled from 'styled-components';
import MainContainer from '../components/containers/MainContainer.js';
import RawMaterials from "../components/RawMaterials.js";
import Popup from "../components/Popup.js";
import CustomDropdown from "../components/CustomDropdown";
import CustomRadioButton from "../components/CustomRadioButton";
import FieldContainer from '../components/containers/FieldContainer.js';
import GradientButton from '../components/GradientButton';

import MaterialsContainer from "../components/containers/MaterialsContainer.js";
import axios from "axios";

class Inventory extends Component {
  constructor(props) {
    super(props);

    this.state = {
      showBikePartModal: false,
      showRawMatModal: false,
      materials: [],
      bikeParts: [],

      materialInvoices: [],
    }

    this.toggleBikeModal = this.toggleBikeModal.bind(this);
    this.toggleMaterialModal = this.toggleMaterialModal.bind(this);
    this.addMaterial = this.addMaterial.bind(this);
    this.createOrder = this.createOrder.bind(this);
  }

  componentDidMount() {
    this.initializeInventoryLists();
  }

  initializeInventoryLists() {
    axios.get('/materials')
    .then(res =>
      this.setState({
        materials: res.data._embedded.materialList, 
      }))
    .catch(err => console.log(err));

    axios.get('/plants')
    .then(res =>
      this.setState({
        bikeParts: res.data._embedded.plantList[0].parts, 
      }))
    .catch(err => console.log(err));

    axios.get('/PurchaseOrders')
    .then(res =>
      this.setState({
        materialInvoices: res.data }))
    .catch(err => console.log(err))
  }

  toggleBikeModal() {
    this.setState({ showBikePartModal: !this.state.showBikePartModal });
  }

  toggleMaterialModal() {
    this.setState({ showRawMatModal: !this.state.showRawMatModal });
  }

  addMaterial(e) {
    // e.preventDefault();

    const form = new FormData(e.target);
    const materialName = form.get("materialName");
    const ramount = form.get("ramount");

    let material = {
      "name": materialName,
      "cost": ramount
    }

    axios.post('/materials', material)
    .then(res =>
      console.log(res.data))
    .catch(err => console.log(err));
  }

  createOrder(e) {
    const form = new FormData(e.target);
    const supplierID = form.get("SupplierID");
    const materialID = form.get("MaterialID");
    const quantity = form.get("rquantity");
    const cost = this.state.materials.filter(i => i.id == materialID)[0].cost;

    let materialOrder = {
      "supplier": {
        "id": supplierID,
        "name": "LSD Supplies"
      },
      "purchaseOrderItems": [
        {
          " quantity": quantity,
          "unitPrice": cost,
          "material": {
              "id": materialID,
              "name": this.state.materials.filter(i => i.id == materialID)[0].name,
              "cost": cost
          }
        }
      ]
    }

    axios.post('/PurchaseOrders', materialOrder);
    // console.log(this.state.materials.filter(i => i.id == materialID)[0].name);
  }


  render() {
    let materialList = <div></div>;
    let bikePartList = <div></div>;
    let materialsForParts = <div></div>;
    let materialInvoices = <div></div>;
    let materialOptions = <option></option>;

    if (this.state.materials.length !== 0) {
      materialsForParts = this.state.materials.map((element, index) => {
        return (
          <CustomRadioButton key={index} id={element.name} value={element.name}>{element.name}</CustomRadioButton>
        );
      });
    }

    if (this.state.materials.length !== 0) {
      materialList = this.state.materials.map((element, index) => {
        return (
          <RawMaterials key={index} title={element.name} cost={element.cost} />
        );
      });
    }

    if (this.state.bikeParts.length !== 0) {
      bikePartList = this.state.bikeParts.map((element, index) => {
        return (
          <RawMaterials key={index} title={element.part.partType} type={element.part.type} cost={element.part.cost} amount={element.quantity} />
        );
      });
    }

    if (this.state.materialInvoices.length !== 0) {
      materialInvoices = this.state.materialInvoices.map((invoice, index) => {
        return (
          <MaterialsContainer
            key={index}
            invoiceID={invoice.id}
            userName={invoice.supplier.name}
            userID={invoice.supplier.id}
            rawMaterial={invoice.purchaseOrderItems.length != 0 ? invoice.purchaseOrderItems[0].material.name : "untitled"}
            totalCost={invoice.grandTotal}
            receptionStatus={invoice.received ? "RECEIVED" : "NOT RECEIVED" }
            paymentStatus={invoice.paid ? "PAID" : "NOT PAID"} />
        );
      });
    }

    if (this.state.materials.length !== 0) {
      materialOptions = this.state.materials.map((material, index) => {
        return (
          <option key={index} value={material.id}>{material.name}</option>
        );
      });
    }

    console.log(this.state.materials);
    return (
      <Container>
        <AddBikeParts isVisible={this.state.showBikePartModal}>
          <Popup showModal={this.toggleBikeModal} title="Bike Part Settings" buttonTitle="add bike(s)" > 
           <form>
              <CustomDropdown dropdownName="bikeParts" dropddownID="bikeParts">
                <option value={"HANDLE"}>handle</option>
                <option value={"SEAT"}>seat</option>
                <option value={"FRAMES"}>frames</option>
                <option value={"WHEEL"}>wheel</option>
              </CustomDropdown>

              <Title>Materials Needed</Title>

              <FieldContainer>
                {materialsForParts}
              </FieldContainer>

              <Title>Amount</Title>
              <FieldContainer>
                <TextInput type="number" id="bamount" name="bamount" placeholder="amount" min ="0"/>
              </FieldContainer>
              <GradientButton type="submit" buttonValue="add bike part" />
           </form>
          </Popup>
        </AddBikeParts>
        <AddRawMaterials isVisible={this.state.showRawMatModal}>
          <Popup showModal={this.toggleMaterialModal} title="Raw Material" buttonTitle="add bike(s)" > 
           <form onSubmit={this.addMaterial}>
              <FieldContainer>
                <TextInput type="text" id="materialName" name="materialName" placeholder="material name" />
              </FieldContainer>
                {/* <CustomDropdown dropdownName="bikeParts" dropddownID="bikeParts">
                  <option value={"HANDLE"}>handle</option>
                  <option value={"SEAT"}>seat</option>
                  <option value={"FRAMES"}>frames</option>
                  <option value={"WHEEL"}>wheel</option>
                </CustomDropdown> */}

              <Title>Amount</Title>
              <FieldContainer>
                <TextInput type="number" id="ramount" name="ramount" placeholder="amount" min ="0" step="0.01" />
              </FieldContainer>
              <GradientButton type="submit" buttonValue="add raw material" />
           </form>
          </Popup>
        </AddRawMaterials>

      <InventoryContainer>
        <MainContainer title="Bike parts" createFeature={true} showModal={this.toggleBikeModal}>
          {bikePartList}
        </MainContainer>
      </InventoryContainer>
      <InventoryContainer>
        <MainContainer title="Raw Material" createFeature={true} showModal={this.toggleMaterialModal}>
          <Legend>
            <div>Part</div>
          </Legend>
          {materialList}
        </MainContainer>
      </InventoryContainer>

      <InventoryContainer>
        <RawMaterialsContainer>
          <MainContainer title="Raw Material Orders">
            {materialInvoices}
          </MainContainer>
     
          <MainContainer title="Order Raw Material">
            <br />
            <form onSubmit={this.createOrder}>
              <CustomDropdown dropdownName="SupplierID" dropddownID="SupplierID">
                <option value={1}>LSD Supplier</option>
              </CustomDropdown>
              <br />
              <CustomDropdown dropdownName="MaterialID" dropddownID="MaterialID">
                {materialOptions}
              </CustomDropdown>
              <br />
              <Title>Quantity</Title>
              <FieldContainer>
                <TextInput type="number" id="rquantity" name="rquantity" placeholder="quantity" min ="0"/>
              </FieldContainer>
              <br></br>
              <GradientButton type="submit" buttonValue="create order" />
            </form>
          </MainContainer>
        </RawMaterialsContainer>
      </InventoryContainer>

      </Container>
    );
  }
}

//STYLED-COMPONENTS

const Container = styled.div`
height: 100%;
border-radius: 0px;
display: flex;
flex-direction: row;
position: relative;

& > div {
  margin-right: 20px;
}
`

const InventoryContainer = styled.div`
  flex-direction: row;
  display: flex;
  flex: 1;
  width: 100%;
  // height: 100%;
`

const RawMaterialsContainer = styled.div`
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;

  & > div {
    flex: 1;
    width: calc(100% - 40px); // TODO: fix fixed-width (100% should work, probably not accessing the right div)
  }

  & > div:nth-child(2) {
    margin-top: 20px;
  }

  & > div:nth-child(1) {
    height: 0;
  }
`

const Title = styled.div`
    
    font-size: 8pt;
    color: black;
    text-transform: uppercase;
    letter-spacing: 0.2em;
    font-weight: 500;
    margin-top: 20px;
`

const AddBikeParts = styled.div`
  position: absolute;
  z-index: 1;
  top: -20px;
  left: -101px;
  display: ${props => props.isVisible ? 'block' : 'none'};

  & > div > div > form > input {
    margin-top: 20px;
  }

  & > div > div > form > div:nth-child(3) {
    padding: 0 0 10px 10px;
  }
`

const AddRawMaterials = styled.div`
  position: absolute;
  z-index: 1;
  top: -20px;
  left: -101px;
  display: ${props => props.isVisible ? 'block' : 'none'};

  & > div > div > form > input {
    margin-top: 20px;
  }
`

const TextInput = styled.input`
border: 0;

font-size: 9pt;
color: #556C99;
text-transform: uppercase;
letter-spacing: 0.2em;
font-weight: 500;
margin: 8px;
width: 400px;

&:focus {
    outline: none;
}

::placeholder {
    color: #BBC8E3;
}
`

const Legend = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  margin-top: 10px;
  background: white;
  font-weight: 500;

  div {
    font-family: Proxima Nova;
  }

  div:nth-child(1), div:nth-child(2) {
    min-width: 120px;
  }

  div:nth-child(3) {
    width: 50px;
  }

  div:nth-child(4) {
    width: 50px;
  }
`


Inventory.propTypes = {
  
};

export default Inventory;