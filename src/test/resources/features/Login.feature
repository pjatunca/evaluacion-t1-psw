Feature: Login de usuarios
  Como usuario registrado
  Quiero iniciar sesión
  Para acceder al sistema

  Scenario: Inicio de sesión correcto
    Given existe un usuario registrado con correo "ana@correo.com" y contraseña "Clave123"
    When ingreso el correo "ana@correo.com" y la contraseña "Clave123"
    Then el inicio de sesión es exitoso y se autentica el correo "ana@correo.com"

  Scenario: Contraseña incorrecta
    Given existe un usuario registrado con correo "ana@correo.com" y contraseña "Clave123"
    When ingreso el correo "ana@correo.com" y la contraseña "OtraClave"
    Then el inicio de sesión falla con el mensaje "Contraseña incorrecta"
