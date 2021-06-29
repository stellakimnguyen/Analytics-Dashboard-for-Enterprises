import React, { Component } from "react";
import PropTypes from "prop-types";
import styled from 'styled-components';
import axios from 'axios';
import MainContainer from '../components/containers/MainContainer.js';
import Popup from "../components/Popup.js";
import InvoiceContainer from "../components/containers/InvoiceContainer.js";
import GradientButton from "../components/GradientButton.js"

class Accounting extends Component {
  constructor(props) {
    super(props);

    this.state = {
      showModal: false,

      invoiceCost: 0,
      invoiceID: 0,

      bikeInvoices: [],
      materialInvoices: [],

      receivableBalance: 0,
      payableBalance: 0,
      payableInvoices: [],
      receivableInvoices:[],
    }

    this.togglePaymentModal = this.togglePaymentModal.bind(this);
    this.getInvoiceDetails = this.getInvoiceDetails.bind(this);
    this.deductAmount = this.deductAmount.bind(this);
    this.initializeAccountingData = this.initializeAccountingData.bind(this);
  }

  togglePaymentModal() {
    this.setState({ showModal: !this.state.showModal });
  }

  deductAmount(e) {
    console.log(`Deduct ${this.state.invoiceCost}$ from account`);
    this.togglePaymentModal();

    axios.put(`/PurchaseOrders/${this.state.invoiceID}/MakePayment`);
  }

  getInvoiceDetails(cost, id) {
    console.log(`Current invoice has a total amount of: ${cost}$`);
    this.setState({
      invoiceCost: cost,
      invoiceID: id });
  }

  makeBikePayment() {
    console.log(this.state.invoiceID);
  }

  initializeAccountingData() {
    axios.get('/PurchaseOrders')
    .then(res =>
      this.setState({
        materialInvoices: res.data }))
    .catch(err => console.log(err))

    axios.get('/SaleOrders')
    .then(res =>
      this.setState({
        bikeInvoices: res.data }))
    .catch(err => console.log(err))

    axios.get('/account/10')
    .then(res =>
      this.setState({
        receivableBalance: res.data.balance }))
    .catch(err => console.log(err))

    axios.get('/account/9')
    .then(res =>
      this.setState({
        payableBalance: res.data.balance }))
    .catch(err => console.log(err))

    axios.get('/ledger')
    .then(res =>
      this.setState({
        payableInvoices: res.data._embedded.ledgerList.filter(i => (i.debitAccount.name == "AccountPayable") ), //TODO (talk to zubair)
        receivableInvoices: res.data._embedded.ledgerList.filter(i => (i.creditAccount.name == "AccountReceivable") ),
      }))
    .catch(err => console.log(err))
  }

  componentDidMount() {
    this.initializeAccountingData();
  }

  render() {
    let materialInvoices = <div></div>;
    let bikeInvoices = <div></div>;
    let payableInvoices = <div></div>;
    let receivableInvoices = <div></div>;

    if (this.state.payableInvoices.length !== 0) {
      payableInvoices = this.state.payableInvoices.map((invoice, index) => {
        return (
          <InvoiceContainer
            key={index}
            invoiceID={invoice.purchaseOrder.id}
            userName={invoice.purchaseOrder.supplier.name}
            userID={invoice.purchaseOrder.supplier.id}
            amount={invoice.purchaseOrder.purchaseOrderItems.length != 0 ? invoice.purchaseOrder.purchaseOrderItems[0].quantity : 1}
            productName={invoice.purchaseOrder.purchaseOrderItems.length != 0 ? invoice.purchaseOrder.purchaseOrderItems[0].material.name : "untitled"}
            payType="OWE"
            totalCost={invoice.purchaseOrder.grandTotal}
            readOnly />
        );
      });
    }

    if (this.state.receivableInvoices.length !== 0) {
      receivableInvoices = this.state.receivableInvoices.map((invoice, index) => {
        return (
          <InvoiceContainer
            key={index}
            invoiceID={invoice.saleOrder.id}
            userName={invoice.saleOrder.client.name}
            userID={invoice.saleOrder.client.id}
            amount={invoice.saleOrder.saleOrderItems.length != 0 ? invoice.saleOrder.saleOrderItems[0].quantity : 1}
            productName={invoice.saleOrder.saleOrderItems.length != 0 ? invoice.saleOrder.saleOrderItems[0].bike.name : "untitled"}
            payType="OWED"
            totalCost={invoice.saleOrder.grandTotal}
            readOnly />
        );
      });
    }

    if (this.state.bikeInvoices.length !== 0) {
      bikeInvoices = this.state.bikeInvoices.map((invoice, index) => {
        return (
          <InvoiceContainer
            key={index}
            invoiceID={invoice.id}
            userName={invoice.client.name}
            userID={invoice.client.id}
            amount={invoice.saleOrderItems.length != 0 ? invoice.saleOrderItems[0].quantity : 1}
            productName={invoice.saleOrderItems.length != 0 ? invoice.saleOrderItems[0].bike.name : "untitled"}
            payType="Bike Cost"
            totalCost={invoice.grandTotal}
            payAction={invoice.paid ? "PAID" : "PAY"}
            productStatus={invoice.shipped ? "SHIPPED" : 'IN PROGRESS'} />
        );
      });
    }
    
    if (this.state.materialInvoices.length !== 0) {
      materialInvoices = this.state.materialInvoices.map((invoice, index) => {
        return (
          <InvoiceContainer
            key={index}
            invoiceID={invoice.id}
            userName={invoice.supplier.name}
            userID={invoice.supplier.id}
            amount={invoice.purchaseOrderItems.length != 0 ? invoice.purchaseOrderItems[0].quantity : 1}
            productName={invoice.purchaseOrderItems.length != 0 ? invoice.purchaseOrderItems[0].material.name : "untitled"}
            payType="Material Cost"
            totalCost={invoice.grandTotal}
            payAction={invoice.paid ? "PAID" : "PAY"}
            productStatus={invoice.received ? "RECEIVED" : "NOT RECEIVED"}
            showModal={this.togglePaymentModal}
            sendInvoiceCost={this.getInvoiceDetails} />
        );
      });
    }

    return (
      <Container>
        <PaymentPopup isVisible={this.state.showModal}>
          <Popup showModal={this.togglePaymentModal} title="ORDER PAYMENT" >
            <form onSubmit={this.deductAmount}>
              Order <InvoiceDetail>#{this.state.invoiceID}</InvoiceDetail> cost <InvoiceDetail>${this.state.invoiceCost}</InvoiceDetail>.
              <br/><br/>
              <InvoiceDetail>${this.state.invoiceCost}</InvoiceDetail> will be deducted from your account.
              <GradientButton type="submit" buttonValue="pay order" />
            </form>
          </Popup>
        </PaymentPopup>
        <TopContainer>
          <MainContainer title="Accounts Payable">
            {payableInvoices}
          </MainContainer>

          <MainContainer title="Accounts Receivable">
            {receivableInvoices}
          </MainContainer>

          <InvoicesContainer>
            <MainContainer title="Bike Invoice">
              {bikeInvoices}
            </MainContainer>
            <MainContainer title="Material Invoice">
              {materialInvoices}
            </MainContainer>
          </InvoicesContainer>
        </TopContainer>

        <BottomContainer>
          <MainContainer title="Accounts Balance">
            <BalanceTitle>Account Receivable: ${this.state.receivableBalance}</BalanceTitle>
            <BalanceTitle>Account Payable: ${this.state.payableBalance}</BalanceTitle>
          </MainContainer>
        </BottomContainer>
      </Container>
    );
  }
}

Accounting.propTypes = {
};

export default Accounting;

//STYLED-COMPONENTS
const TopContainer = styled.div`
  flex-direction: row;
  display: flex;
  flex: 2;
  width: 100%;

  & > div {
    flex: 1;
    margin-right: 20px;
  }

  & > div:nth-child(3) {
    margin-right: 0;
     & > div {
       height: 0;
     }
  }
`

const InvoicesContainer = styled.div`
  display: flex;
  flex-direction: column;
  width: 100%;

  & > div {
    flex: 1;
    width: calc(100% - 40px); // TODO: fix fixed-width (100% should work, probably not accessing the right div)
    max-height: 400px; //TODO: Quickfix
  }

  & > div:nth-child(2) {
    margin-top: 20px;
  }
`

const BottomContainer = styled.div`
  flex: 1;
  margin-top: 20px;
  
  & > div {
    width: calc(100% - 40px);
  }

  & > div > div:nth-child(2) > div:nth-child(1) {
    color: #3BC351;
  }

  & > div > div:nth-child(2) > div:nth-child(2) {
    color: #FF7A67;
  }
`

const Container = styled.div`
  height: 100%;
  width: 100%;
  border-radius: 0px;
  display: flex;
  flex-direction: column;
  position: relative;
`

const PaymentPopup = styled.div`
  position: absolute;
  z-index: 1;
  top: -20px;
  left: -101px;
  display: ${props => props.isVisible ? 'block' : 'none'};

  & > div > div > form > input {
    margin-top: 20px;
  }

  & > div > div {
    width: 275px;
  }
`

const InvoiceDetail = styled.div`
  font-weight: 500;
  color: #FF7A67;
  text-transform: uppercase;
  display: inline-block;
`

const BalanceTitle = styled.div`
  font-weight: 500;
  font-size: 26pt;
  margin-top: 20px;
  text-transform: uppercase;
  letter-spacing: 0.15em;
`