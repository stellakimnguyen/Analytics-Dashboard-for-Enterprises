import './App.css';
import React from 'react';
import styled from 'styled-components';
import NavigationContainer from './components/Navigation.js';
import Main from './Main.js';

function App() {
  return (
    <Container>
      <NavigationContainer />
      <Content>
        {/* Insert selected page component here */}
        <Main />
      </Content>
    </Container>
  );
}

//STYLED-COMPONENTS
const Container = styled.div`
  background: #F2F5FC;
  height: 100vh;
  display: flex;
  flex-direction: row;
`

const Content = styled.div`
  padding: 20px;
  width: 100%;
`

export default App;
