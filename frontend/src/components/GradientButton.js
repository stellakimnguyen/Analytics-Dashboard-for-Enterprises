import React, { Component } from "react";
import PropTypes from "prop-types";
import styled from 'styled-components';

class GradientButton extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
        <CustomButton type="submit" value={this.props.buttonValue} />
    );
  }
}

// STYLED-COMPONENTS
const CustomButton = styled.input`
    border-radius: 12px;
    padding: 15px;
    display: flex;
    align-items: center;
    justify-content: center;
    border: 0;
    color: white;
    font-size: 10pt;
    color: white;
    text-transform: uppercase;
    letter-spacing: 0.2em;
    font-weight: 500;
    width: 100%;
    background: linear-gradient(to right, #FF7A67, #FFE2C8);
    cursor: pointer;
    transition: 1000ms;

    &:focus {
        outline: none;
    }

    &:hover {
        background: #FF7A67;
    }
`

GradientButton.propTypes = {
};

export default GradientButton;