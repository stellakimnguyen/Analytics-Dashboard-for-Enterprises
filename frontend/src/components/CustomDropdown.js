import React, { Component } from "react";
import PropTypes from "prop-types";
import styled from 'styled-components';

class CustomDropdown extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
        <Container name={this.props.dropdownName} id={this.props.dropdownID} onChange={this.props.handleChange} onLoad={() => this.props.handleChange}>
            {this.props.children}
        </Container>
    );
  }
}

//STYLED-COMPONENTS
const Container = styled.select`
  background: white;
  padding: 7px;
  width: 100%;
  border-radius: 8px;
  margin-bottom: 5px;
  border: solid 2px #BBC8E3;
  display: flex;
  flex-direction: column;

  
  font-size: 9pt;
  color: '#556C99';
  text-transform: lowercase;
  letter-spacing: 0.2em;
  font-weight: 500;
`

CustomDropdown.propTypes = {
    children: PropTypes.element.isRequired,
};

export default CustomDropdown;
