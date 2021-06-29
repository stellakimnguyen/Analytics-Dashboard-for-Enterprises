import React, { Component } from "react";
import PropTypes from "prop-types";
import styled from 'styled-components';
import InnerContainer from '../components/containers/InnerContainer.js';
import MainContainer from '../components/containers/MainContainer.js';
import FieldContainer from '../components/containers/FieldContainer.js';
import CustomRadioButton from '../components/CustomRadioButton.js';

class CreateUser extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
        <MainContainer title="Creating User">
            <InnerContainer title="New User">
                <NewUserForm action="">
                    <div>
                        <FieldContainer>
                            <TextInput type="text" id="fname" name="firstname" placeholder="first name" />
                        </FieldContainer>
                        <FieldContainer>
                            <TextInput type="text" id="lname" name="lastname" placeholder="last name" />
                        </FieldContainer>
                        <FieldContainer>
                            <TextInput type="email" id="email" name="email" placeholder="email" />
                        </FieldContainer>
                        <FieldContainer>
                            <TextInput type="password" id="password" name="password" placeholder="password" />
                        </FieldContainer>
                        <FieldContainer>
                            <TextInput type="password" id="password" name="confirmpassword" placeholder="confirm password" />
                        </FieldContainer>

                        <Title>Permission</Title>
                        <CustomRadioButton value="admin">Admin</CustomRadioButton>
                        <CustomRadioButton value="highaccess">High Access Employee</CustomRadioButton>
                        <CustomRadioButton value="medaccess">Medium Access Employee</CustomRadioButton>
                        <CustomRadioButton value="lowaccess">Low Access Employee</CustomRadioButton>
                    </div>
                    <SubmitButton type="submit" value="Create User" />
                </NewUserForm>
            </InnerContainer>
        </MainContainer>
    );
  }
}

//STYLED-COMPONENTS
const NewUserForm = styled.form`
    display: flex;
    flex-direction: column;
    height: calc(100% - 20px);
    justify-content: space-between;
`

const TextInput = styled.input`
    border: 0;
    
    font-size: 9pt;
    color: #556C99;
    text-transform: uppercase;
    letter-spacing: 0.2em;
    font-weight: 500;
    margin: 8px;
    width: 400px;

    &:focus {
        outline: none;
    }

    ::placeholder {
        color: #BBC8E3;
    }
`
const Title = styled.div`
    
    font-size: 10pt;
    color: black;
    text-transform: uppercase;
    letter-spacing: 0.2em;
    font-weight: 500;
    margin-top: 20px;
`

const SubmitButton = styled.input`
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

CreateUser.propTypes = {
    // innerTitle: PropTypes.string.isRequired,
    // mainTitle: PropTypes.string.isRequired,
    children: PropTypes.element.isRequired,
};

export default CreateUser;
