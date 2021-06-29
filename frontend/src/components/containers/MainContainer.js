import React, { Component } from "react";
import PropTypes from "prop-types";
import styled from 'styled-components';
import AddIcon from '@material-ui/icons/Add';

class MainContainer extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
        <Container>
          <Header>
            <Title>{this.props.title}</Title>
            <AddButton onClick={this.props.showModal} isVisible={this.props.createFeature}>
              <AddIcon />
            </AddButton>
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
  height: calc(100% - 40px);
  box-shadow: 0 0 30px 0 rgba(43, 64, 104, 0.1);
  position: relative;
  width: fit-content;

  button {
    // background-color: transparent;
    background-repeat: no-repeat;
    border: none;
    cursor: pointer;
    overflow: hidden;
    outline: none;
    // color: #FF7A67;
    transition: 250ms;
    // padding: 0;

    // &:hover, &::selection {
    //   color: #BBC8E3;
    // }

    // svg {
    //   width: 1.2em;
    //   height: 1.2em;
    // }
}
`

const Content = styled.div`
  overflow-y: auto;
  height: calc(100% - 20px);
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
    // font-family: Proxima Nova;
    font-size: 12pt;
    color: black;
    text-transform: uppercase;
    letter-spacing: 0.2em;
    font-weight: 500;
`

const AddButton = styled.div`
  background-color: transparent;
  background-repeat: no-repeat;
  border: none;
  cursor: pointer;
  overflow: hidden;
  outline: none;
  color: #FF7A67;;
  transition: 250ms;
  padding: 0;
  display: ${props => props.isVisible ? 'block' : 'none'};

  &:hover, &::selection {
    color: #BBC8E3;
  }
`

MainContainer.propTypes = {
    title: PropTypes.string.isRequired,
    children: PropTypes.element.isRequired,
};

export default MainContainer;
