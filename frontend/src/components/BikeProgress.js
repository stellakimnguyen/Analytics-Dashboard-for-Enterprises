import React, { Component } from "react";
import PropTypes from "prop-types";
import styled from 'styled-components';
import StepProgressBar from 'react-step-progress';
import 'react-step-progress/dist/index.css';
import axios from "axios";

class BikeProgress extends Component {
  constructor(props) {
    super(props);

    this.state = {
      bikeProgress: 0,
      plantBikes: [],
      shipmentStatus: 0,
    }

    this.initializePlantBikes = this.initializePlantBikes.bind(this);
    this.currentBikeStep = this.currentBikeStep.bind(this);
    this.makeValidator = this.makeValidator.bind(this);
  }

  componentDidMount() {
    this.initializePlantBikes();
    
    axios.get(`/SaleOrders/${this.props.saleOrderID}`)
      .then(res => res.data)
      .then(data => {
        if (data.shipped == true) {
          console.log('we are here!!!!!!!!!!');
          this.setState({ shipmentStatus: 2 });
        } else {
          console.log('we are here');
          this.setState({ shipmentStatus: 0 });
        }
      })
  }

  initializePlantBikes() {
    axios.get('/plants/1')
    .then(res =>
      this.setState({
        plantBikes: res.data, }))
    .catch(err => console.log(err))
  }

  gatherValidator() {
    axios.post(`/SaleOrders/${this.props.saleOrderID}/GatherBikeParts`)
      .then(res => alert(res.data.message))

    return true;
  }

  makeValidator() {
    axios.post(`/SaleOrders/${this.props.saleOrderID}/MakeBike`)
      .then(res => alert(res.data.message))
    return true;
  }

  shippingValidator() {
    axios.post(`/SaleOrders/${this.props.saleOrderID}/ShipBike`)
      .then(res => alert(res.data.message))
    return true;
  }

  onFormSubmit() {
    alert("Bike is in shipment!");
  }

  currentBikeStep(status) {
    // axios.get(`/SaleOrders/${this.props.saleOrderID}`)
    // .then(res =>
    //   this.setState({ shipmentStatus: res.data.shipped })
    // )

    if (status == true) {
      console.log('we are here!!!!!!!!!!');
      this.setState({ shipmentStatus: 2 });
    } else {
      console.log('we are here');
      this.setState({ shipmentStatus: 0 });
    }
  }

  render() {
    const step1Content = <h1>Step 1 Content</h1>;
    const step2Content = <h1>Step 2 Content</h1>;
    const step3Content = <h1>Step 3 Content</h1>;
    
    console.log(this.state.shipmentStatus);

    return (
        <Container
            startingStep={this.state.shipmentStatus}
            onSubmit={this.onFormSubmit}
            steps={[
                {
                    label: 'gather',
                    name: 'step 1',
                    content: step1Content,
                    onSubmit: this.gatherValidator,
                },
                {
                    label: 'make',
                    name: 'step 2',
                    content: step2Content,
                    validator: this.makeValidator,
                },
                {
                    label: 'shipping',
                    name: 'step 3',
                    content: step3Content,
                    validator: this.shippingValidator
                },
            ]}
        />
    );
  }
}

//STYLED-COMPONENTS
const Container = styled(StepProgressBar)`
  width: 100%;

  & > ul {
    margin-top: 35px;
  }
`

BikeProgress.propTypes = {
    
};

export default BikeProgress;
