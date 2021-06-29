import React, { Component } from "react";
import PropTypes from "prop-types";
import styled from 'styled-components';

import ExpandLessMore from '@material-ui/icons/ExpandMore';
import FrameIcon from '../../icons/bikeframe.svg';
import FormatPaintIcon from '@material-ui/icons/FormatPaint';
import LayersIcon from '@material-ui/icons/Layers';
import HandleIcon from '../../icons/handlebar.svg';
import PedalIcon from '../../icons/pedal.svg';
import BikeProgress from '../BikeProgress.js'

class BikeContainer extends Component {
  constructor(props) {
    super(props);

     this.state = {
      showExpansion: false,
      progressLevel: "assemble",
    }

    this.toggleExpansion = this.toggleExpansion.bind(this);
  }

  toggleExpansion() {
    this.setState({ showExpansion: !this.state.showExpansion });
  }

  render() {
    return (
        <Container isExpanded={this.state.showExpansion}>
            <Header>
              <div>
                <Title>{this.props.title}</Title>
                <PaymentStatus>&nbsp;- {this.props.paidStatus ? "PAID" : "NOT PAID"}</PaymentStatus>
              </div>
              <ExpandButton onClick={this.toggleExpansion} isExpanded={this.state.showExpansion}>
                <ExpandLessMore />                
              </ExpandButton>
            </Header>
            <CharacteristicContainer>
              <Characteristic>
                <img src={FrameIcon} />
                <PartTitle>{this.props.size}</PartTitle>
              </Characteristic>
              <Characteristic>
                <ColorIcon frameColor={this.props.frameColor} />
                <PartTitle>{this.props.frameColor}</PartTitle>
              </Characteristic>
              <Characteristic>
                <FormatPaintIcon color="#BBC8E3"/>
                <PartTitle>{this.props.finish}</PartTitle>
              </Characteristic>
              <Characteristic>
                <LayersIcon color="#BBC8E3"/>
                <PartTitle>{this.props.grade}</PartTitle>
              </Characteristic>
              <Characteristic>
                <img src={HandleIcon} />
                <PartTitle>{this.props.handlebar}</PartTitle>
              </Characteristic>
              <Characteristic>
                <img src={PedalIcon} />
                <PartTitle>{this.props.pedal}</PartTitle>
              </Characteristic>
            </CharacteristicContainer>
            {/* {this.props.children} */}
            <ProgressContainer isExpanded={this.state.showExpansion} >
              <BikeProgress saleOrderID={this.props.saleOrderID} bikeID={this.props.bikeID} progressLevel={this.state.progressLevel} />
            </ProgressContainer>
        </Container>
    );
  }
}

//STYLED-COMPONENTS

const Container = styled.div`
  background: #F9F9F9;
  padding: 15px;
  border-radius: 12px;
  height: ${props => props.isExpanded ? '200px' : '100px'};
  width: 506px;
  margin-top: 15px;
  transition: 250ms;
`

const Header = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;

  &  > div:nth-child(1) {
    display: flex;
  }
`

const Title = styled.div`
    font-size: 9pt;
    color: black;
    text-transform: uppercase;
    letter-spacing: 0.2em;
    font-weight: 500;
`

const PaymentStatus = styled(Title)`
  color: grey;
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
  transform:  ${props => props.isExpanded ? 'rotate(180deg)': 'rotate(0deg)'};
  transition: 250ms;

  &:hover, &::selection {
    color: #BBC8E3;
  }

  svg {
    width: 1.2em;
    height: 1.2em;
  }
`

const CharacteristicContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
`

const Characteristic = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;

  & > img {
    width: 3em;
    height: 3em;
  }

  & > svg {
    width: 1.7em;
    height: 1.7em;
    color: #BBC8E3;
  }
`

const PartTitle = styled.div`
  font-weight: 500;
  font-size: 8pt;
  letter-spacing: 0.2em;
  color: #556C99;
  text-transform: uppercase;
`

const ColorIcon = styled.div`
  border-radius: 100%;
  width: 2.5em;
  height: 2.5em;
  background-color: ${props => props.frameColor ? props.frameColor : 'blue'};
`

const ProgressContainer = styled.div`
  display:  ${props => props.isExpanded ? 'block' : 'none'};
`

BikeContainer.propTypes = {
    innerTitle: PropTypes.string.isRequired,
    children: PropTypes.element.isRequired,
};

export default BikeContainer;
