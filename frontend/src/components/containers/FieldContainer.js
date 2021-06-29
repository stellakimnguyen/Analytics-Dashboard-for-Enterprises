import React, { Component } from "react";
import PropTypes from "prop-types";
import styled from 'styled-components';

class FieldContainer extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
        <Container textColor={this.props.textColor} >
            {this.props.children}
        </Container>
    );
  }
}

//STYLED-COMPONENTS
const Container = styled.div`
  background: white;
  padding: 5px;
  border-radius: 12px;
  margin-top: 15px;
  border: solid 2px #BBC8E3;
  max-height: 400px;
  overflow-y: auto;

  
  font-size: 9pt;
  color: ${props => props.textColor ? props.textColor : '#556C99'};
  text-transform: uppercase;
  letter-spacing: 0.2em;
  font-weight: 500;
`

FieldContainer.propTypes = {
    children: PropTypes.element.isRequired,
};

export default FieldContainer;
