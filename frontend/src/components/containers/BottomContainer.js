import React, { Component } from "react";
import PropTypes from "prop-types";
import styled from 'styled-components';

class BottomContainer extends Component {
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
  left: 0;
  top: 570px;
  height: calc(100% - 620px);
  width: calc(100% - 20px);
  box-shadow: 0 0 30px 0 rgba(43, 64, 104, 0.1);
  position: absolute;

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
    
    font-size: 12pt;
    color: black;
    text-transform: uppercase;
    letter-spacing: 0.2em;
    font-weight: 500;
`

BottomContainer.propTypes = {
    title: PropTypes.string.isRequired,
    children: PropTypes.element.isRequired,
    createFeature: PropTypes.bool.isRequired,
};

export default BottomContainer;
