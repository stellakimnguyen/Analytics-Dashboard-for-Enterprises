import React, { Component } from "react";
import PropTypes from "prop-types";
import styled from 'styled-components';
import axios from 'axios';

class MaterialsContainer extends Component {
  constructor(props) {
    super(props);
    this.receivedStatus = this.receivedStatus.bind(this);
  }

  receivedStatus() {
    axios.put(`/PurchaseOrders/${this.props.invoiceID}/ReceiveMaterial`);
  }


  render() {
    return (
        <Container>
            <LeftHandside>
              <Title>INVOICE #{this.props.invoiceID}</Title>
              
              <GeneralInfoContainer>
                {this.props.userName} #{this.props.userID} <br/>
                {this.props.rawMaterial} 
              </GeneralInfoContainer>

              <PriceContainer>
                 ${this.props.totalCost}
              </PriceContainer>
            </LeftHandside>
            <RightHandside hidden={this.props.readOnly}>
              <StatusTitle>
                {this.props.paymentStatus}
              </StatusTitle>
              <form onSubmit={this.receivedStatus}>
                <ReceiveButton type="submit" receptionStatus={this.props.receptionStatus} value={this.props.receptionStatus} />
              </form>
            </RightHandside>
        </Container>
    );
  }
}

//STYLED-COMPONENTS
const Container = styled.div`
  background: #F9F9F9;
  padding: 15px;
  border-radius: 12px;
  margin-top: 15px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
`

const LeftHandside = styled.div`
  & > div:nth-child(2), & > div:nth-child(3) {
    
    font-size: 7pt;
    font-weight: 100;
    margin-top: 5px;
    text-transform: uppercase;
    letter-spacing: 0.2em;
  }
`

const RightHandside = styled.div`
  display: ${props => props.hidden ? 'none' : 'flex'};
  flex-direction: column;
  align-items: flex-end;
`

const ReceiveButton = styled.input`
  background-color: ${props => props.receptionStatus == ("RECEIVED") ? '#CCCCCC' : '#A6CEE3'};
  color: white;
  font-family: Proxima Nova;
  font-size: 7pt;
  text-transform: uppercase;
  letter-spacing: 0.2em;
  border-radius: 4px;
  padding: 5px 10px;
  margin-top: 5px;
  pointer-events: ${props => props.receptionStatus == ("RECEIVED") ? 'none' : 'auto'};

  background-repeat: no-repeat;
  border: none;
  cursor: pointer;
  overflow: hidden;
  outline: none;
  transition: 250ms;

  &:hover {
    background-color: ${props => props.receptionStatus == ("NOT RECEIVED") ? '#588ca8' : '#CCCCCC'};
  }
`

const StatusTitle = styled.div`
  font-size: 8pt;
  color: #A3A3A3;
  text-transform: uppercase;
  letter-spacing: 0.2em;
  font-weight: 500;
`

const Title = styled.div`
  
  font-size: 8pt;
  color: black;
  text-transform: uppercase;
  letter-spacing: 0.2em;
  font-weight: 500;
`

const GeneralInfoContainer = styled.div`
  font-weight: 400;
`

const PriceContainer = styled.div`
  font-weight: 400;
`

MaterialsContainer.propTypes = {
};

export default MaterialsContainer;
