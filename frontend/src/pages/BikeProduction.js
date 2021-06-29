import React, { Component } from "react";
import PropTypes from "prop-types";
import styled from 'styled-components';
import axios from "axios";
import MainContainer from '../components/containers/MainContainer.js';
import BikeContainer from "../components/containers/BikeContainer.js";
import FieldContainer from '../components/containers/FieldContainer.js';
import Popup from "../components/Popup.js";
import CustomDropdown from "../components/CustomDropdown";
import GradientButton from "../components/GradientButton.js"

import ToggleOnIcon from '@material-ui/icons/ToggleOn';

class BikeProduction extends Component {
  constructor(props) {
    super(props);

    this.state = {
      showModal: false,
      bikes: [],
      bikeParts: [],
    }

    this.toggleBikeModal = this.toggleBikeModal.bind(this);
    this.initializeBikes = this.initializeBikes.bind(this);
    this.findIndexForPart = this.findIndexForPart.bind(this);
    this.findIndexForFrame = this.findIndexForFrame.bind(this);
  }

  toggleBikeModal() {
    this.setState({ showModal: !this.state.showModal });
  }

  initializeBikes() {
    axios.get('/SaleOrders')
    .then(res =>
      this.setState({
        bikes: res.data,
        bikeParts: res.data }))
    .catch(err => console.log(err))
  }

  addBike(e) {
    // e.preventDefault();

    const form = new FormData(e.target);
    const bikeSize = form.get("bikeSize");
    const bikeColor = form.get("bikeColor");
    const bikeFinish = form.get("bikeFinish");
    const bikeGrade = form.get("bikeGrade");
    const bikeHandlebar = form.get("bikeHandlebar");
    const bikePedal = form.get("bikePedal");
    const bikeName = form.get("bikeName");

    let plantBike = {
      "bike": {
          "name": bikeName,
          "handlebar": {
              "partType": "handlebar",
              "id": bikeHandlebar,
              "name": null,
              "cost": 0.0,
              "materials": [],
              "type": null
          },
          "frame": {
              "partType": "frame",
              "id": bikeColor,
              "name": null,
              "cost": 0.0,
              "materials": [],
              "colour": null,
              "size": bikeSize, //hardcode
              "finish": bikeFinish //hardcode
          },
          "frontwheel": {
              "partType": "wheel",
              "id": 80,
              "name": "frontwheel121",
              "cost": 32.0,
              "materials": [],
              "diameter": 2.0,
              "gear": false
          },
          "rearwheel": {
              "partType": "wheel",
              "id": 83,
              "name": null,
              "cost": 0.0,
              "materials": [],
              "diameter": 0.0,
              "gear": false
          },
          "seat": {
              "partType": "seat",
              "id": 81,
              "name": null,
              "cost": 0.0,
              "materials": [],
              "size": 0
          },
          "pedal": {
              "partType": "pedal",
              "id": bikePedal,
              "name": null,
              "cost": 0.0,
              "materials": [],
              "type": null
          },
          "accessories": []
      },
      "quantity": 2
    }

    axios.post('/SaleOrders', plantBike);
    console.log(bikeName + ": " + bikeSize + " " + bikeColor + " " + bikeFinish + " " + bikeGrade + " " + bikeHandlebar + " " + bikePedal);
  }

  componentDidMount() {
    this.initializeBikes();
  }

  findIndexForPart(bikePart, partName) {
    const index = this.state.bikeParts.findIndex(item => (item.part.partType === bikePart && item.part.type === partName));
    const id = this.state.bikeParts[index];
    return id;
  }

  findIndexForFrame(frameColour) {
    const index = this.state.bikeParts.findIndex(item => (item.part.partType === "frame" && item.part.colour === frameColour));
    const id = this.state.bikeParts[index].part.id;
    return id;
  }

  render() {
    console.log(this.state.bikes);

    let bikeList = <div></div>;

    if (this.state.bikes.length !== 0) {
      bikeList = this.state.bikes.map((bike, index) => {
        return (
          <BikeContainer
            key={index}
            paidStatus={bike.paid}
            title={bike.saleOrderItems.length !== 0 ? bike.saleOrderItems[0].bike.name : "unnamed"}
            size={bike.saleOrderItems.length !== 0 ? bike.saleOrderItems[0].bike.frame.size : 26}
            frameColor={bike.saleOrderItems.length !== 0 ? bike.saleOrderItems[0].bike.frame.colour : "blue"}
            finish={bike.saleOrderItems.length !== 0 ? bike.saleOrderItems[0].bike.frame.finish : "matte"}
            grade={"aluminium"}
            handlebar={bike.saleOrderItems.length !== 0 ? bike.saleOrderItems[0].bike.handlebar.type : "straight"}
            pedal={bike.saleOrderItems.length !== 0 ? bike.saleOrderItems[0].bike.pedal.type : "strap"}
            saleOrderID={bike.id}
            bikeID={bike.saleOrderItems[0].bike.id}
            />
        );
      });
    }

    console.log(this.state.bikeParts);
    
    return (
      //TODO: Separate containers into components
      <Container>
        <AddBikePopup isVisible={this.state.showModal}>
          <Popup showModal={this.toggleBikeModal} title="Bike settings" > 
            <form onSubmit={this.addBike}>
              <FieldContainer>
                <TextInput type="text" id="bikeName" name="bikeName" placeholder="bike name" />
              </FieldContainer>
              <CustomDropdown dropdownName="bikeSize" dropddownID="bikeSize">
                <option value={24.0}>small</option>
                <option value={26.0}>medium</option>
                <option value={28.0}>large</option>
              </CustomDropdown>
              <CustomDropdown dropdownName="bikeColor" dropddownID="bikeColor">
                <option value={96}>red</option>
                <option value={14}>blue</option>
                <option value={85}>green</option>
                {/* <option value={"ORANGE"}>orange</option>
                <option value={"SILVER"}>silver</option>
                <option value={"BLACK"}>black</option> */}
              </CustomDropdown>
              <CustomDropdown dropdownName="bikeFinish" dropddownID="bikeFinish">
                <option value={"MATTE"}>matte</option>
                <option value={"CHROME"}>chrome</option>
              </CustomDropdown>
              <CustomDropdown dropdownName="bikeGrade" dropddownID="bikeGrade">
                <option value={"STEEL"}>steel</option>
                <option value={"ALUMINIUM"}>aluminium</option>
                <option value={"CARBON"}>carbon</option>
              </CustomDropdown>
              <CustomDropdown dropdownName="bikeHandlebar" dropddownID="bikeHandlebar">
                <option value={84}>dropbar</option>
                <option value={61}>straight</option>
                <option value={62}>bullhorn</option>
              </CustomDropdown>
              <CustomDropdown dropdownName="bikePedal" dropddownID="bikePedal">
                <option value={12}>strap</option>
                <option value={82}>clip</option>
              </CustomDropdown>  
              <FieldContainer>
                <TextInput type="quantity" id="bikeQuantity" name="bikeQuantity" placeholder="quantity" />
              </FieldContainer>
              <FieldContainer>
                <TextInput type="clientID" id="clientID" name="clientID" placeholder="client ID" />
              </FieldContainer>
              <GradientButton type="submit" buttonValue="add bike(s)" />
            </form>
          </Popup>
        </AddBikePopup>
        <MainContainer title="Bikes" createFeature={true} showModal={this.toggleBikeModal}>
          {/* TODO: Add interactive toggle
              <div>
              <button><ToggleOnIcon /></button>
              <Title>In Progress</Title>
          </div> */}
          
          {/* <BikeContainer title="Bike #SerialID" /> */}
          {bikeList}
        </MainContainer>
      </Container>
    );
  }
}


//STYLED-COMPONENTS
const Container = styled.div`
  position: relative;
  height: 100%;
`

const Title = styled.div`
    
    font-size: 10pt;
    color: black;
    text-transform: uppercase;
    letter-spacing: 0.2em;
    font-weight: 500;
    margin-top: 20px;
    
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

const AddBikePopup = styled.div`
  position: absolute;
  z-index: 2; //over bike progress
  top: -20px;
  left: -101px;
  display: ${props => props.isVisible ? 'block' : 'none'};

  & > div > div > form > input {
    margin-top: 20px;
  }

  & > div > div > form > div:nth-child(1) {
    margin-bottom: 15px;
  }
`

BikeProduction.propTypes = {
};

export default BikeProduction;