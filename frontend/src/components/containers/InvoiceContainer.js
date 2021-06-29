import React, { Component } from "react";
import PropTypes from "prop-types";
import styled from 'styled-components';
import axios from 'axios';

class InvoiceContainer extends Component {
  constructor(props) {
    super(props);

    this.updatePaymentStatus = this.updatePaymentStatus.bind(this);
  }

  updatePaymentStatus(e) {
    e.preventDefault();
    if (this.props.payType == "Bike Cost") {
      //TODO: Update payment status for bike invoice
      /////// Add amount to company's account
      axios.put(`/SaleOrders/${this.props.invoiceID}/ReceivePayment`);
      console.log(`${this.props.totalCost}$ has been added to the company's account.`);
    } else if (this.props.payType == "Material Cost") {
      this.props.showModal();
      this.props.sendInvoiceCost(this.props.totalCost, this.props.invoiceID);
    }
  }

  render() {
    return (
        <Container>
            <LeftHandside>
              <Title>INVOICE #{this.props.invoiceID}</Title>
              {/* {this.props.children} */}
              
              <GeneralInfoContainer>
                {this.props.userName} #{this.props.userID} <br/>
                {this.props.amount} x {this.props.productName}
              </GeneralInfoContainer>

              <PriceContainer>
                {this.props.payType}: ${this.props.totalCost}
              </PriceContainer>
            </LeftHandside>
            <RightHandside hidden={this.props.readOnly}>
              <form onSubmit={this.updatePaymentStatus}>
                <PayActionButton type="submit" payAction={this.props.payAction} value={this.props.payAction} />
              </form>
              <StatusTitle>
                {this.props.productStatus}
              </StatusTitle>
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

const PayActionButton = styled.input`
  background-color: ${props => props.payAction == ("PAID") ? '#3BC351' : '#FF7A67'};
  color: white;
  font-family: Proxima Nova;
  font-size: 7pt;
  text-transform: uppercase;
  letter-spacing: 0.2em;
  border-radius: 4px;
  padding: 5px 20px;
  margin-bottom: 5px;
  pointer-events: ${props => props.payAction == ("PAY") ? 'auto' : 'none'};

  background-repeat: no-repeat;
  border: none;
  cursor: pointer;
  overflow: hidden;
  outline: none;
  transition: 250ms;

  &:hover {
    background-color: ${props => props.payAction == ("PAY") ? '#F44A32' : '#3BC351'};
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

InvoiceContainer.propTypes = {
};

export default InvoiceContainer;
