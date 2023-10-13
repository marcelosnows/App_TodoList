package br.com.marcelosnows.todolist.utils;

import java.beans.PropertyDescriptor;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Utils {

  // Será atribuído para esse BeanUtils para a mescla das informações. 
  public static void copyNonNullProperties(Object source, Object target) {
      BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
  };
  
  // Pegando tudo que for de properties nulls. Alterando a classe para "static" não é necessário instancia-la.
  public static String[] getNullPropertyNames(Object source) {
      
      final BeanWrapper src = new BeanWrapperImpl(source);

      // Obtendo um array com todas as propriedades 
      PropertyDescriptor[] pds = src.getPropertyDescriptors();

      // criando um conjunto com as propriedades com value null
      Set<String> emptyNames = new HashSet<>();

      // interação das properties descriptor 
      for(PropertyDescriptor pd: pds) {

        // para cada property value, pega o getName e obtem o valor da propriedade atual.
        Object srcValue = src.getPropertyValue(pd.getName());
        if(srcValue == null) {

          // todas as properties com valor null serão inseridas no emptyNames
          emptyNames.add(pd.getName());
        };
      };

      // pegando e convertendo os nomes das properties para um array de string.
      String[] result = new String[emptyNames.size()];
      return emptyNames.toArray(result);
    };
  };

  // BeanWrapper - Interface do Java que permite acessar propriedades de objetos
  // BeanWrapperImpl - Implementação dessa interface.
