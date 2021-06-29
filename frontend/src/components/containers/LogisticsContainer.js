import React, { Component } from "react";
import PropTypes from "prop-types";
import styled from 'styled-components';

class LogisticsContainer extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
        <Container>
          <Header>
            <Title>{this.props.title}</Title>
          </Header>
          <Content>
            {this.props.children}
          </Content>
        </Container>
    );
  }
}

//STYLED-COMPONENTS
const Container = styled.div`
  background: white;
  padding: 20px;
  border-radius: 12px;
  width: 40;
  height: calc(100% - 40px);
  box-shadow: 0 0 30px 0 rgba(43, 64, 104, 0.1);
  display: flex;
  flex-direction: column;
}
`

const Content = styled.div`
  height: calc(100% - 20px);

  & > div:nth-child(2) {
    display: flex;
    flex-direction: column;
    overflow-y: auto;
    height: calc(100% - 130px);
  }

  button {
    background-color: transparent;
    background-repeat: no-repeat;
    border: none;
    cursor: pointer;
    overflow: hidden;
    outline: none;
    color: #FF7A67;
    transition: 250ms;
    padding: 0;

    &:hover, &::selection {
      color: #BBC8E3;
    }
`

const Header = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  background: white;
`

const Title = styled.div`
    
    font-size: 12pt;
    color: black;
    text-transform: uppercase;
    letter-spacing: 0.2em;
    font-weight: 500;
`

LogisticsContainer.propTypes = {
    title: PropTypes.string.isRequired,
    children: PropTypes.element.isRequired,
    createFeature: PropTypes.bool.isRequired,
};

export default LogisticsContainer;
