import React, { Component } from "react";
import PropTypes from "prop-types";
import styled from 'styled-components';

class CustomRadioButton extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
        <Container>
            <CheckboxContainer>
                <input type="radio" name={this.props.name} id={this.props.id} value={this.props.value} defaultChecked={this.props.defaultChecked} />
                <Checkmark />
                {this.props.children}
            </CheckboxContainer>
        </Container>
    );
  }
}

//STYLED-COMPONENTS
const Container = styled.div`
    
    font-size: 10pt;
    color: #556C99;
    text-transform: uppercase;
    letter-spacing: 0.2em;
    font-weight: 500;
    display: flex;
    align-items: center;
`

const Checkmark = styled.div`
    position: relative;
    height: 18px;
    width: 18px;
    background-color: #eee;
    border-radius: 50%;
    margin-right: 10px;
`

const CheckboxContainer = styled.label`
    display: flex;
    flex-direction: row;
    align-items: center;
    position: relative;
    margin-top: 12px;
    cursor: pointer;
    user-select: none;

    & input {
        position: absolute;
        opacity: 0;
        cursor: pointer;

        &:checked ~ ${Checkmark} {
            box-sizing: border-box;
            border: 3px #556C99 solid;
            background-color: white;
            transition: 250ms ease-out;

            &::after {
                display: block;
            }
        }
    }

    &::after {
        top: 9px;
        left: 9px;
        width: 8px;
        height: 8px;
        border-radius: 50%;
        background: white;
    }
`

CustomRadioButton.propTypes = {
    children: PropTypes.element.isRequired,
    name: PropTypes.string.isRequired,
};

export default CustomRadioButton;
