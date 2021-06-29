import React, { Component } from "react";
import PropTypes from "prop-types";
import styled from 'styled-components';
import ExpandLessMore from '@material-ui/icons/ExpandMore';

class RawMaterials extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <Container>
        <Header>
            <Title>{this.props.title}</Title>
            <Title>{this.props.type}</Title>
            <Title>{this.props.cost}</Title>
            <Title>{this.props.amount}</Title>
        </Header>
        {this.props.children}
      </Container>
    );
  }
}


//STYLED-COMPONENTS
const Container = styled.div`
  background: #F9F9F9;
  padding: 20px;
  border-radius: 12px;
  height: calc(100%-109px);
  width: 506px;
  margin-top: 15px;
`

const Header = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;

  div:nth-child(1), div:nth-child(2) {
    min-width: 120px;
  }

  div:nth-child(3) {
    width: 60px;
  }

  div:nth-child(4) {
    width: 30px;
  }
`

const Title = styled.div`
    
    font-size: 9pt;
    color: black;
    text-transform: uppercase;
    letter-spacing: 0.2em;
    font-weight: 500;
`

const ExpandButton = styled.div`
  background-color: transparent;
  background-repeat: no-repeat;
  border: none;
  cursor: pointer;
  overflow: hidden;
  outline: none;
  color: #BBC8E3;
  transition: 250ms;
  display: block;

  &:hover, &::selection {
    color: #BBC8E3;
  }
`

RawMaterials.propTypes = {
  innerTitle: PropTypes.string.isRequired,
  children: PropTypes.element.isRequired,
};

export default RawMaterials;