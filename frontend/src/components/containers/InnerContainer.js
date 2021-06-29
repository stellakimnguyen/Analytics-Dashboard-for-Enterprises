import React, { Component } from "react";
import PropTypes from "prop-types";
import styled from 'styled-components';

class InnerContainer extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
        <Container>
            <Title>{this.props.title}</Title>
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
  height: 100%;
  margin-top: 15px;
`

const Title = styled.div`
    
    font-size: 11pt;
    color: black;
    text-transform: uppercase;
    letter-spacing: 0.2em;
    font-weight: 500;
`

InnerContainer.propTypes = {
    innerTitle: PropTypes.string.isRequired,
    children: PropTypes.element.isRequired,
};

export default InnerContainer;
